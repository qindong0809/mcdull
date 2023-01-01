package com.dqcer.framework.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha1工具
 *
 * @author dqcer
 * @version 2022/10/04
 */
@SuppressWarnings("unused")
public class Sha1Util {

    private static final Logger log = LoggerFactory.getLogger(Sha1Util.class);

    /**
     * 禁止实例化
     */
    private Sha1Util() {
        throw new AssertionError();
    }


    /**
     * 获取sha1 摘要后的值
     *
     * @param message 消息
     * @return {@link String}
     */
    public static String getSha1(String message) {
        try {
            byte[] result = MessageDigest.getInstance("SHA1").digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
