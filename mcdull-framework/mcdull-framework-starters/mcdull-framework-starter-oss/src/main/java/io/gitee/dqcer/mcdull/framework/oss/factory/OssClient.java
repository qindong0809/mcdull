package io.gitee.dqcer.mcdull.framework.oss.factory;

public interface OssClient {

    void upload(byte[] content, String fileName, String bucket);
}
