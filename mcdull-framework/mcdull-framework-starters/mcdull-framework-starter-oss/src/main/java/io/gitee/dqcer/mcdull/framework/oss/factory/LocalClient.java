package io.gitee.dqcer.mcdull.framework.oss.factory;


import io.gitee.dqcer.mcdull.framework.config.properties.OssProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地文件存储
 *
 * @author dqcer
 * @since 2023/03/22
 */
public class LocalClient implements OssClient{

    private static final Logger log = LoggerFactory.getLogger(LocalClient.class);

    private final OssProperties properties;

    public LocalClient(OssProperties properties) {
        this.properties = properties;
    }

    @Override
    public void upload(byte[] content, String fileName, String bucket) {
        // TODO: 2023/3/22
        log.info("本地文件存储 ....");
//        FileUtil.touch()
    }

}
