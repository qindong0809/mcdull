package io.gitee.dqcer.mcdull.framework.oss.component;

import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.OssProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.OssTypeEnum;
import io.gitee.dqcer.mcdull.framework.oss.factory.LocalClient;
import io.gitee.dqcer.mcdull.framework.oss.factory.OssClient;
import io.gitee.dqcer.mcdull.framework.oss.factory.QiniuClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * oss
 *
 * @author dqcer
 * @since 2023/03/21
 */
@Component
public class OssFactory {

    @Resource
    private McdullProperties properties;

    public OssClient getInstance() {
        OssProperties oss = properties.getOss();
        OssTypeEnum type = oss.getType();
        if (type.equals(OssTypeEnum.QINIU)) {
            return new QiniuClient(oss);
        }
        return new LocalClient(oss);
    }
}
