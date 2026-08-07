package io.gitee.mcdull.tools.web.service.source;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Random access over a remote file via SFTP.
 * <p>
 * SFTP natively supports reading at an arbitrary offset ({@code SSH_FXP_READ} carries offset + length),
 * so the byte cursor paging algorithm works identically to local.
 *
 * @author dqcer
 */
public class SftpLogReader implements LogReader {

    private final ChannelSftp channel;

    private final String remotePath;

    public SftpLogReader(ChannelSftp channel, String remotePath) {
        this.channel = channel;
        this.remotePath = remotePath;
    }

    @Override
    public long size() {
        try {
            SftpATTRS attrs = channel.stat(remotePath);
            return attrs.getSize();
        } catch (SftpException e) {
            throw new BusinessException("Failed to stat remote file " + remotePath + ": " + e.getMessage());
        }
    }

    @Override
    public void readFully(long offset, byte[] into, int intoOffset, int length) throws IOException {
        // ChannelSftp.get with RESUME mode positions at offset
        try (InputStream is = channel.get(remotePath, null, offset)) {
            int totalRead = 0;
            while (totalRead < length) {
                int n = is.read(into, intoOffset + totalRead, length - totalRead);
                if (n < 0) {
                    throw new IOException("Unexpected end of file at offset " + (offset + totalRead)
                            + " of " + remotePath);
                }
                totalRead += n;
            }
        } catch (SftpException e) {
            throw new IOException("SFTP read failed on " + remotePath + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        // Do not close the channel — it belongs to the SftpLogSource and is shared across readers.
    }
}
