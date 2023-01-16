package com.dqcer.mcdull.framework.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * md5工具
 *
 * @author dqcer
 * @version 2022/10/04
 */
@SuppressWarnings("unused")
public class Md5Util {

    private static final Logger log = LoggerFactory.getLogger(Md5Util.class);

    /**
     * 禁止实例化
     */
    private Md5Util() {
        throw new AssertionError();
    }

    /**
     * 获取md5
     *
     * @param message 消息
     * @return {@link String}
     */
    public static String getMd5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            byte[] md5Byte = md.digest(messageByte);
            md5 = bytesToHex(md5Byte);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return md5;
    }


    /**
     * 二进制转十六进制
     *
     * @param bytes 字节
     * @return {@link String}
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        int num;
        for (byte aByte : bytes) {
            num = aByte;
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
