package io.gitee.dqcer.mcdull.framework.web.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时区
 *
 * @author dqcer
 * @since 2024/06/20
 */
public class TimeZoneUtil {

    public static String serializeDate(Date date, String dateFormat) {
        return serializeDate(date, dateFormat, LocaleContextHolder.getLocale(), null, true);
    }

    public static String serializeDate(Date date, String dateFormat, boolean splicingTimezone) {
        return serializeDate(date, dateFormat, LocaleContextHolder.getLocale(), null, splicingTimezone);
    }

    public static String serializeDate(Date date, String dateFormat, Locale locale, String zoneIdStr) {
        return serializeDate(date, dateFormat, locale, zoneIdStr, true);
    }

    public static String serializeDate(Date date, String dateFormat, String zoneIdStr, boolean appendTimezoneStyle) {
        Locale locale = LocaleContextHolder.getLocale();
        return serializeDate(date, dateFormat, locale, zoneIdStr, appendTimezoneStyle);
    }

    /**
     * 序列化日期
     *
     * {@link ZoneId#SHORT_IDS}
     *
     * @param date             日期
     * @param zoneIdStr        zoneIdStr
     * @param dateFormat       日期格式
     * @param locale           locale
     * @param splicingTimezone 拼接时区
     * @return {@link String}
     */
    public static String serializeDate(Date date,
                                       String dateFormat,
                                       Locale locale,
                                       String zoneIdStr,
                                       boolean splicingTimezone) {
        DateTime dateTime = DateTime.of(date);
        if (StrUtil.isBlank(zoneIdStr)) {
            zoneIdStr = "UTC";
        }
        ZoneId zoneId = ZoneId.of(zoneIdStr);
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        DateTime convertTimeZone = DateUtil.convertTimeZone(dateTime, timeZone);
        String result = convertTimeZone.toString(DateUtil.newSimpleFormat(dateFormat, locale, timeZone));
        if (splicingTimezone) {
            ZoneRules rules = zoneId.getRules();
            ZoneOffset offset = rules.getOffset(date.toInstant());
            String offsetId = offset.getId();
            final String offsetZ = "Z";
            if (offsetZ.equals(offsetId)) {
                offsetId = "+00:00";
            }
            result = StrUtil.format("{}({})", result, offsetId);
        }
        return result;
    }

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_WITHOUT_YEAR = "dd-MMM-yyyy";

    public static final String DATE_TIME_FORMAT_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";

    public static void main(String[] args) {
        System.out.println(serializeDate(new Date(),  DATE_TIME_FORMAT, Locale.ENGLISH, "Asia/Shanghai",true));
        System.out.println(serializeDate(new Date(),  DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH, "-05:00",false));
        System.out.println(serializeDate(new Date(),  DATE_TIME_FORMAT, Locale.ENGLISH, "-01:00",true));
        System.out.println(serializeDate(new Date(),  DATE_TIME_FORMAT, Locale.ENGLISH, "Asia/Ho_Chi_Minh",true));
        System.out.println(serializeDate(new Date(),  DATE_TIME_FORMAT_WITHOUT_SECOND, Locale.ENGLISH, "Asia/Ho_Chi_Minh",false));
    }
}
