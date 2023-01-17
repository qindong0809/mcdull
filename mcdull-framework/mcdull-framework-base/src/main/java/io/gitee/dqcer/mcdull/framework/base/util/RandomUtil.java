package io.gitee.dqcer.mcdull.framework.base.util;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

/**
 * 随机数工具类
 *
 * @author dqcer
 * @date 2022/12/19
 */
public class RandomUtil {

    private static final SecureRandom NUMBER_GENERATOR = new SecureRandom();

    public static int random(int min, int max) {
        return min + NUMBER_GENERATOR.nextInt(max - min);
    }

    /**
     * 产生1~9位随机数字
     *
     * @param len 要产生的随机数的长度，取值范围：1~9
     * @return int
     */
    public static int gen09(int len) {
        if (len < GlobalConstant.Number.NUMBER_1 || len > GlobalConstant.Number.NUMBER_9) {
            throw new IllegalArgumentException("len must be 1~9");
        }
        int d = (int) Math.pow(10, len - 1);
        return d + new Random().nextInt(d * 9);
    }

    /**
     * 产生1~32 小写字母和数字的随机字符串
     *
     * @param len 要产生的随机字符串的长度，取值范围：a~z0~9。长度为32时等同于UUID
     * @return
     */
    public static String genAz09(int len) {
        int max = 32;
        if (len < 1 || len > max) {
            throw new IllegalArgumentException("len must be 1~32");
        }
        String s = uuid();
        return s.substring(32 - len);
    }


    /**
     * 从UUID复制出来的方法，不生成-，所以比genUUID更快
     *
     * @return
     */
    @SuppressWarnings("all")
    public static String uuid() {
        /* random */
        byte[] randomBytes = new byte[16];
        NUMBER_GENERATOR.nextBytes(randomBytes);
        // clear version
        randomBytes[6] &= 0x0f;
        // set to version 4
        randomBytes[6] |= 0x40;
        // clear variant
        randomBytes[8] &= 0x3f;
        // set to IETF variant
        randomBytes[8] |= 0x80;

        /* uuid */
        long msb = 0;
        long lsb = 0;
        assert randomBytes.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);
        }

        return toString(msb, lsb);
    }

    private static String toString(long mostSigBits, long leastSigBits) {
        return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4)
                + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public static String random(Map<String, Integer> map) {
        int total = 0;
        for (Integer w : map.values()) {
            total += (w == null ? 0 : w);
        }
        int i = RandomUtil.random(0, total);
        for (Map.Entry<String, Integer> z : map.entrySet()) {
            if (z.getValue() >= i) {
                return z.getKey();
            }
            i -= z.getValue();
        }
        return null;
    }
}
