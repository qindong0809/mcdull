package io.gitee.mcdull.tools.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Log viewer settings.
 * <p>
 * Supports multiple targets. When no targets are configured, a single local target is created from the
 * legacy {@code dir}/{@code activeFile} settings for backward compatibility.
 *
 * @author dqcer
 */
@Data
@Component
@ConfigurationProperties(prefix = "mcdull.tools.log")
public class LogProperties {

    /**
     * Directory holding the log files (legacy, used when targets is empty).
     */
    private String dir = "";

    /**
     * File that the live tail follows, relative to {@link #dir} (legacy).
     */
    private String activeFile = "out.log";

    private String charset = "UTF-8";

    /**
     * Lines returned per history page, and the amount of context replayed when a live tail starts.
     */
    private int pageLines = 300;

    /**
     * Upper bound for a single keyword scan.
     */
    private long maxScanBytes = 32L * 1024 * 1024;

    private long tailIntervalMillis = 500L;

    /**
     * SSE comment interval, keeps idle connections from being dropped by proxies.
     */
    private long heartbeatMillis = 15_000L;

    /**
     * Named credentials, referenced by targets via the {@code credential} field.
     */
    private Map<String, CredentialProperties> credentials = new LinkedHashMap<>();

    /**
     * Targets available for browsing. Each target is one "env + service" entry in the UI selector.
     */
    private List<TargetProperties> targets = new ArrayList<>();

    @Data
    public static class CredentialProperties {

        private String username;

        private String password;

        /**
         * Path to the SSH private key file (PEM format). When set, key auth is used and password
         * is treated as the key passphrase (if the key is encrypted).
         */
        private String privateKeyPath;
    }

    @Data
    public static class TargetProperties {

        /**
         * Stable identifier, used in URLs.
         */
        private String id;

        /**
         * Display label: environment name.
         */
        private String env = "本机";

        /**
         * Display label: service name.
         */
        private String service = "";

        /**
         * Remote host. Empty means local.
         */
        private String host = "";

        private int port = 22;

        /**
         * References a key in {@link LogProperties#credentials}.
         */
        private String credential = "";

        /**
         * Directory holding the log files.
         */
        private String dir = "";

        /**
         * Glob pattern filtering files inside dir. Only matching files are listed.
         */
        private String pattern = "*.log*";

        /**
         * File the live tail follows, relative to dir.
         */
        private String activeFile = "out.log";

        private String charset = "UTF-8";

        /**
         * @return true when this target points to a remote host
         */
        public boolean isRemote() {
            return StrUtil.isNotBlank(host);
        }
    }
}
