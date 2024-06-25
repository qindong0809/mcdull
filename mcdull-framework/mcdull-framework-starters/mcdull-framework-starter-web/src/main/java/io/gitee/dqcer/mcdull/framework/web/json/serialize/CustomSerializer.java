package io.gitee.dqcer.mcdull.framework.web.json.serialize;

import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;

import java.util.Date;
import java.util.Locale;

/**
 * 自定义序列化程序
 *
 * @author dqcer
 * @since 2024/06/20
 */
public class CustomSerializer {

    private CustomSerializer() {
    }

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_WITHOUT_YEAR = "dd-MMM-yyyy";

    public static final String DATE_TIME_FORMAT_WITHOUT_SECOND = "yyyy-MM-dd HH:mm";


    public static CustomSerializer getInstance() {
//        return CustomSerializerHolder.INSTANCE;
        return null;
    }

    public static String serializeDate(Date date, String dateFormat, Locale locale, String zoneIdStr) {
        return TimeZoneUtil.serializeDate(date, dateFormat, locale, zoneIdStr, true);
    }

    public static String serializeDate(UnifySession session, Date date, boolean splicingTimezone) {
        String dateCellValue = CustomSerializer.serializeDate(UserContextHolder.getSession(), date, true);
        String dateFormat = session.getDateFormat();
        String zoneIdStr = session.getZoneIdStr();
        Locale locale = session.getLocale();
        return TimeZoneUtil.serializeDate(date, dateFormat, locale, zoneIdStr, splicingTimezone);
    }


}
