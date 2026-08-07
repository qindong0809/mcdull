package io.gitee.mcdull.tools.web.service.source;

import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * Random access over a local file.
 *
 * @author dqcer
 */
public class LocalLogReader implements LogReader {

    private final RandomAccessFile file;

    private final Path path;

    public LocalLogReader(Path path) {
        this.path = path;
        try {
            this.file = new RandomAccessFile(path.toFile(), "r");
        } catch (IOException e) {
            throw new BusinessException("Failed to open log file " + path.getFileName(), e);
        }
    }

    @Override
    public long size() {
        try {
            return file.length();
        } catch (IOException e) {
            throw new BusinessException("Failed to read the size of " + path.getFileName(), e);
        }
    }

    @Override
    public void readFully(long offset, byte[] into, int intoOffset, int length) throws IOException {
        file.seek(offset);
        file.readFully(into, intoOffset, length);
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
