package io.gitee.dqcer.mcdull.framework.config.properties;

/**
 * oss属性
 *
 * @author dqcer
 * @since 2023/03/21
 */
public class OssProperties {

    private OssTypeEnum type = OssTypeEnum.LOCAL;

    private String url;

    private String accessKey;

    private String accessSecretKey;


    public OssTypeEnum getType() {
        return type;
    }

    public void setType(OssTypeEnum type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecretKey() {
        return accessSecretKey;
    }

    public void setAccessSecretKey(String accessSecretKey) {
        this.accessSecretKey = accessSecretKey;
    }
}
