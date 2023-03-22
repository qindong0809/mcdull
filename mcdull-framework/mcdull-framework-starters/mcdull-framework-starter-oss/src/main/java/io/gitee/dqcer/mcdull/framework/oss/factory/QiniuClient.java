package io.gitee.dqcer.mcdull.framework.oss.factory;


import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.gitee.dqcer.mcdull.framework.config.properties.OssProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 七牛云
 *
 * @author dqcer
 * @since 2023/03/22
 */
public class QiniuClient implements OssClient{

    private static final Logger log = LoggerFactory.getLogger(QiniuClient.class);

    private final OssProperties properties;

    public QiniuClient(OssProperties properties) {
        this.properties = properties;
    }

    @Override
    public void upload(byte[] content, String fileName, String bucket) {
        Configuration cfg = new Configuration(Region.huanan());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(properties.getAccessKey(), properties.getAccessSecretKey());
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(content, fileName, upToken);
            DefaultPutRet putRet = response.jsonToObject(DefaultPutRet.class);
            String s = properties.getUrl() + "/" + putRet.key;
            log.info("七牛云 oss: {}", s);
        } catch (QiniuException e) {
            throw new RuntimeException(e);
        }
    }
}
