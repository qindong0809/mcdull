package io.gitee.mcdull.tools.web.service.source;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.config.LogProperties.CredentialProperties;
import io.gitee.mcdull.tools.config.LogProperties.TargetProperties;
import io.gitee.mcdull.tools.web.domain.LogFileVO;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Reads log files from a remote host over SFTP.
 * <p>
 * The Session and ChannelSftp are created once and held until {@link #close()} is called or the
 * connection drops. The registry checks liveness and recreates as needed.
 *
 * @author dqcer
 */
@Slf4j
public class SftpLogSource implements LogSource {

    private static final int CONNECT_TIMEOUT_MS = 10_000;

    private static final int CHANNEL_TIMEOUT_MS = 5_000;

    private final TargetProperties config;

    private final CredentialProperties credential;

    private Session session;

    private ChannelSftp channel;

    public SftpLogSource(TargetProperties config, CredentialProperties credential) {
        this.config = config;
        this.credential = credential;
        this.connect();
    }

    @Override
    public List<LogFileVO> list() {
        this.ensureConnected();
        String dir = config.getDir();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + config.getPattern());
        List<LogFileVO> result = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Vector<ChannelSftp.LsEntry> entries = channel.ls(dir);
            String activeName = config.getActiveFile();
            for (ChannelSftp.LsEntry entry : entries) {
                SftpATTRS attrs = entry.getAttrs();
                if (attrs.isDir()) {
                    continue;
                }
                String name = entry.getFilename();
                if (!matcher.matches(Paths.get(name))) {
                    continue;
                }
                LogFileVO vo = new LogFileVO();
                vo.setName(name);
                vo.setSize(attrs.getSize());
                vo.setLastModified((long) attrs.getMTime() * 1000L);
                vo.setActive(name.equals(activeName));
                result.add(vo);
            }
        } catch (SftpException e) {
            throw new BusinessException("Failed to list remote log directory " + dir + " on " + config.getHost()
                    + ": " + e.getMessage());
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
        this.ensureConnected();
        String name = (file == null || file.isBlank()) ? config.getActiveFile() : file;
        if (name.contains("..")) {
            throw new BusinessException("Illegal file name: " + name);
        }
        String remotePath = config.getDir() + "/" + name;
        return new SftpLogReader(channel, remotePath);
    }

    @Override
    public Charset charset() {
        try {
            return Charset.forName(config.getCharset());
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    public boolean isConnected() {
        return session != null && session.isConnected()
                && channel != null && channel.isConnected();
    }

    public void close() {
        if (channel != null) {
            try { channel.disconnect(); } catch (Exception ignored) { }
            channel = null;
        }
        if (session != null) {
            try { session.disconnect(); } catch (Exception ignored) { }
            session = null;
        }
    }

    private void connect() {
        try {
            JSch jsch = new JSch();
            // Enable JSch debug logging temporarily
            JSch.setLogger(new com.jcraft.jsch.Logger() {
                @Override
                public boolean isEnabled(int level) { return level >= com.jcraft.jsch.Logger.WARN; }
                @Override
                public void log(int level, String message) {
                    LogHelp.info(log, "JSch: {}", message);
                }
            });
            // Key auth takes priority over password auth
            String keyPath = credential.getPrivateKeyPath();
            if (keyPath != null && !keyPath.isBlank()) {
                String passphrase = credential.getPassword();
                if (passphrase != null && !passphrase.isBlank()) {
                    jsch.addIdentity(keyPath, passphrase);
                } else {
                    jsch.addIdentity(keyPath);
                }
            }
            session = jsch.getSession(credential.getUsername(), config.getHost(), config.getPort());
            if (keyPath == null || keyPath.isBlank()) {
                // Password auth
                session.setPassword(credential.getPassword());
            }
            // Strict host key checking is disabled for internal tooling. This is acceptable because:
            // 1. Only test environment targets, configured by the team
            // 2. Internal network, not facing the internet
            session.setConfig("StrictHostKeyChecking", "no");
            // Accept all host key types including ED25519
            session.setConfig("server_host_key", "ssh-ed25519,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521,rsa-sha2-512,rsa-sha2-256,ssh-rsa");
            session.connect(CONNECT_TIMEOUT_MS);

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(CHANNEL_TIMEOUT_MS);
            LogHelp.info(log, "SFTP connected: {}@{}:{}", credential.getUsername(), config.getHost(), config.getPort());
        } catch (JSchException e) {
            this.close();
            throw new BusinessException("Failed to connect to " + config.getHost() + ":" + config.getPort()
                    + " — " + e.getMessage());
        }
    }

    private void ensureConnected() {
        if (!this.isConnected()) {
            this.close();
            this.connect();
        }
    }
}
