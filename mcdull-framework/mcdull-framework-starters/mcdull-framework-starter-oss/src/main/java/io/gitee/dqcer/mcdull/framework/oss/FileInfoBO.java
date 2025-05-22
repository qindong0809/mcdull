package io.gitee.dqcer.mcdull.framework.oss;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import org.dromara.x.file.storage.core.FileInfo;

public class FileInfoBO extends FileInfo implements BO {

    private String fileKey;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
