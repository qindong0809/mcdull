package io.gitee.dqcer.util;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.blaze.domain.bo.CertificateBO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 证书 util
 *
 * @author dqcer
 * @since 2025/01/08
 */
public class CertificateUtil {

    public static Map<Integer, CertificateBO> getCertificateMap() {
        String sJson = ResourceUtil.readUtf8Str("certificate.json");
        List<CertificateBO> list = JSONUtil.toList(sJson, CertificateBO.class);
        return list.stream().collect(Collectors.toMap(CertificateBO::getCode, v -> v));
    }
}
