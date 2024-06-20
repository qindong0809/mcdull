package io.gitee.dqcer.mcdull.framework.web.json.serialize;

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


}
