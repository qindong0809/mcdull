package io.gitee.mcdull.tools.web.service.source;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.config.LogProperties.TargetProperties;
import io.gitee.mcdull.tools.web.domain.LogFileVO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Log files on the disk of the host running this application.
 * <p>
 * Instantiated per target by the registry rather than being a singleton, so multiple local targets
 * with different directories can coexist.
 *
 * @author dqcer
 */
@Slf4j
public class LocalLogSource implements LogSource {

    private final TargetProperties config;

    public LocalLogSource(TargetProperties config) {
        this.config = config;
    }

    @Override
    public List<LogFileVO> list() {
        Path root = this.root();
        String activeName = config.getActiveFile();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + config.getPattern());
        List<LogFileVO> result = new ArrayList<>();
        try (Stream<Path> stream = Files.list(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .forEach(path -> {
                        LogFileVO vo = new LogFileVO();
                        String name = path.getFileName().toString();
                        vo.setName(name);
                        vo.setActive(name.equals(activeName));
                        try {
                            vo.setSize(Files.size(path));
                            vo.setLastModified(Files.getLastModifiedTime(path).toMillis());
                        } catch (IOException e) {
                            LogHelp.warn(log, "failed to stat log file. file: {}, reason: {}", name, e.getMessage());
                        }
                        result.add(vo);
                    });
        } catch (IOException e) {
            LogHelp.error(log, "failed to list log directory. dir: {}", root, e);
            throw new BusinessException("Failed to list the log directory");
        }
        result.sort(Comparator.comparingLong(LogFileVO::getLastModified).reversed());
        return result;
    }

    @Override
    public String activeFile() {
        return config.getActiveFile();
    }

    @Override
    public LogReader open(String file) {
        return new LocalLogReader(this.resolve(file));
    }

    @Override
    public Charset charset() {
        try {
            return Charset.forName(config.getCharset());
        } catch (Exception e) {
            LogHelp.warn(log, "unsupported charset, falling back to UTF-8. charset: {}", config.getCharset());
            return StandardCharsets.UTF_8;
        }
    }

    private Path resolve(String fileName) {
        Path root = this.root();
        String name = StrUtil.isBlank(fileName) ? config.getActiveFile() : fileName;
        if (name.contains("/") || name.contains("\\") || name.contains("..")) {
            throw new BusinessException("Illegal log file name: " + name);
        }
        Path target = root.resolve(name).normalize();
        if (!root.equals(target.getParent())) {
            throw new BusinessException("Illegal log file name: " + name);
        }
        if (!Files.isRegularFile(target)) {
            throw new BusinessException("Log file does not exist: " + name);
        }
        return target;
    }

    private Path root() {
        String dir = config.getDir();
        if (StrUtil.isBlank(dir)) {
            throw new BusinessException("Log directory is not configured for target " + config.getId());
        }
        Path root = Paths.get(dir).toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) {
            throw new BusinessException("Log directory does not exist: " + root);
        }
        return root;
    }
}
