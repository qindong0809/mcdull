package io.gitee.dqcer.mcdull.framework.config.properties;

/**
 * oss属性
 *
 * @author dqcer
 * @since 2023/03/21
 */
public class OssProperties {

    public static final Long DEFAULT_MAX_FILE_SIZE = 5L;
    public static final Long DEFAULT_MAX_REQUEST_SIZE = 5L;

    private OssTypeEnum type = OssTypeEnum.LOCAL;

    private static String localPath;

    private String url;

    private String accessKey;

    private String accessSecretKey;

    private Long maxFileSize;

    private Long maxRequestSize;

    public static String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        OssProperties.localPath = localPath;
    }

    public Long getMaxFileSize() {
        return maxFileSize == null ? DEFAULT_MAX_FILE_SIZE : maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxRequestSize() {
        return maxRequestSize == null ? DEFAULT_MAX_REQUEST_SIZE : maxRequestSize;
    }

    public void setMaxRequestSize(Long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

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
