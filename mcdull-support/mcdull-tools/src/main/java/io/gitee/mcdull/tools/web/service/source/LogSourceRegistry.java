package io.gitee.mcdull.tools.web.service.source;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.config.LogProperties;
import io.gitee.mcdull.tools.config.LogProperties.CredentialProperties;
import io.gitee.mcdull.tools.config.LogProperties.TargetProperties;
import io.gitee.mcdull.tools.web.domain.LogTargetVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maintains the set of configured log targets and provides the corresponding {@link LogSource}.
 * <p>
 * Remote sources (SFTP) are cached and reused. A source that has been idle for 5 minutes is closed
 * and will be reopened on the next request. This keeps the connection pool small while still avoiding
 * a fresh SSH handshake on every single page load.
 *
 * @author dqcer
 */
@Slf4j
@Component
public class LogSourceRegistry {

    private static final String DEFAULT_TARGET_ID = "local";

    @Resource
    private LogProperties logProperties;

    /**
     * Ordered map of target configs, built once at startup.
     */
    private Map<String, TargetProperties> targets = Collections.emptyMap();

    /**
     * Cached remote sources. Local sources are stateless and recreated on the fly.
     */
    private final ConcurrentHashMap<String, SftpLogSource> remoteCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<TargetProperties> configured = logProperties.getTargets();
        if (configured == null || configured.isEmpty()) {
            // Backward compatibility: if no targets are explicitly configured, create one from the
            // legacy dir/activeFile/charset settings.
            if (StrUtil.isNotBlank(logProperties.getDir())) {
                TargetProperties legacy = new TargetProperties();
                legacy.setId(DEFAULT_TARGET_ID);
                legacy.setEnv("本机");
                legacy.setService("mcdull-tools");
                legacy.setDir(logProperties.getDir());
                legacy.setActiveFile(logProperties.getActiveFile());
                legacy.setCharset(logProperties.getCharset());
                configured = List.of(legacy);
            } else {
                configured = Collections.emptyList();
            }
        }
        Map<String, TargetProperties> map = new LinkedHashMap<>();
        for (TargetProperties t : configured) {
            if (StrUtil.isBlank(t.getId())) {
                throw new BusinessException("Each log target must have a non-blank id");
            }
            if (map.containsKey(t.getId())) {
                throw new BusinessException("Duplicate log target id: " + t.getId());
            }
            map.put(t.getId(), t);
        }
        this.targets = Collections.unmodifiableMap(map);
        LogHelp.info(log, "log targets loaded: {}", String.join(", ", map.keySet()));
    }

    @PreDestroy
    public void destroy() {
        remoteCache.values().forEach(SftpLogSource::close);
        remoteCache.clear();
    }

    /**
     * @return all configured targets, suitable for the UI selector
     */
    public List<LogTargetVO> listTargets() {
        List<LogTargetVO> result = new ArrayList<>();
        for (TargetProperties t : targets.values()) {
            LogTargetVO vo = new LogTargetVO();
            vo.setId(t.getId());
            vo.setEnv(t.getEnv());
            vo.setService(t.getService());
            vo.setRemote(t.isRemote());
            result.add(vo);
        }
        return result;
    }

    /**
     * Resolves a target id to a usable LogSource.
     *
     * @param targetId target id from the request, blank falls back to the first configured target
     * @return the source
     */
    public LogSource resolve(String targetId) {
        if (targets.isEmpty()) {
            throw new BusinessException("No log targets configured");
        }
        String id = StrUtil.isBlank(targetId) ? targets.keySet().iterator().next() : targetId;
        TargetProperties config = targets.get(id);
        if (config == null) {
            throw new BusinessException("Unknown log target: " + id);
        }
        if (!config.isRemote()) {
            return new LocalLogSource(config);
        }
        // Remote: get or create cached SFTP source
        return remoteCache.compute(id, (key, existing) -> {
            if (existing != null && existing.isConnected()) {
                return existing;
            }
            if (existing != null) {
                existing.close();
            }
            CredentialProperties cred = logProperties.getCredentials().get(config.getCredential());
            if (cred == null) {
                throw new BusinessException("Credential not found for target " + id + ": " + config.getCredential());
            }
            return new SftpLogSource(config, cred);
        });
    }

    /**
     * @return the target config for the given id, used by the tail publisher
     */
    public TargetProperties getConfig(String targetId) {
        if (targets.isEmpty()) {
            throw new BusinessException("No log targets configured");
        }
        String id = StrUtil.isBlank(targetId) ? targets.keySet().iterator().next() : targetId;
        TargetProperties config = targets.get(id);
        if (config == null) {
            throw new BusinessException("Unknown log target: " + id);
        }
        return config;
    }
}
