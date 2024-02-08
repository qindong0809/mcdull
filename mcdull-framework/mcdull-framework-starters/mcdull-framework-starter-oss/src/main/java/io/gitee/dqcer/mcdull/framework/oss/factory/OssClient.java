package io.gitee.dqcer.mcdull.framework.oss.factory;

/**
 * @author dqcer
 */
public interface OssClient {

    void upload(byte[] content, String fileName, String bucket);
}
