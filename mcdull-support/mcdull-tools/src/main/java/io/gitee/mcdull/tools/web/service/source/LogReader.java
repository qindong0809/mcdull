package io.gitee.mcdull.tools.web.service.source;

import java.io.Closeable;
import java.io.IOException;

/**
 * Random access handle on one log file.
 * <p>
 * Reading by offset rather than streaming from the head is what lets history paging cost the same on
 * a 2GB file as on a small one. SFTP supports offset reads natively, so a remote implementation fits
 * this contract without changing any of the paging logic above it.
 *
 * @author dqcer
 */
public interface LogReader extends Closeable {

    /**
     * Current length. Queried live rather than cached, because the active file grows while it is read.
     *
     * @return size in bytes
     */
    long size();

    /**
     * Reads exactly {@code length} bytes starting at {@code offset}.
     *
     * @param offset      absolute byte offset in the file
     * @param into        destination buffer
     * @param intoOffset  offset within the destination buffer
     * @param length      number of bytes to read
     * @throws IOException when fewer than {@code length} bytes are available
     */
    void readFully(long offset, byte[] into, int intoOffset, int length) throws IOException;
}
