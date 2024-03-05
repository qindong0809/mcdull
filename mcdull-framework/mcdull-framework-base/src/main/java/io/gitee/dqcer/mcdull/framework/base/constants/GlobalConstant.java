package io.gitee.dqcer.mcdull.framework.base.constants;

/**
 * 全局常量
 *
 * @author dqcer
 * @since 2022/10/26
 */
@SuppressWarnings("unused")
public class GlobalConstant {

    public static final String BASE_PACKAGE = "io.gitee.dqcer.mcdull";
    public static final String ROOT_PREFIX = "mcdull";

    /**
     * 内部接口调用 前缀
     */
    public static final String INNER_API = "/interior-api";

    public static final String FAVICON_ICO = "/favicon.ico";
    public static final String ACTUATOR_ALL = "/actuator/**";

    public static final String SERVICE_MDC = "/metadata";

    public static final String ALL_PATTERNS = "/**";

    public static final String LOGIN_URL = "/login";

    public static final String LOGIN_CAPTCHA_IMAGE = "/captchaImage";

    /**
     * 服务器临时路径目录
     */
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");


    /**
     * 管理人员类型
     */
    public static final Integer SUPER_ADMIN_USER_TYPE = 2;

    /**
     * 超级管理员角色
     */
    public static final String SUPER_ADMIN_ROLE = "admin";


    /**
     * excel导出最大行
     */
    public static final Long EXCEL_EXPORT_MAX_ROW = 1048576L;



    public static class Environment {

        /**
         * 生产环境
         */
        public static final String PROD = "prod";
    }


    public static class Database {

        /**
         * 数字0： 如数据库操作所影响行数
         */
        public static final int ROW_0 = 0;

        /**
         * 针对查询、更新、删除 sql单条sql 限制1
         */
        public static final String SQL_LIMIT_1 = "LIMIT 1";
    }

    public static class Order {

        /**
         * aop redis lock
         */
        public static final int ASPECT_REDIS_LOCK = -100;

        /**
         * aop cache expire
         */
        public static final int ASPECT_CACHE_EXPIRE = -200;

        /**
         * aop datasource
         */
        public static final int ASPECT_DATA_SOURCE = -50;

        /**
         * aop operation log
         */
        public static final int ASPECT_OPERATION_LOG = -20;


        /**
         * 拦截器基础
         */
        public static final int INTERCEPTOR_BASE = -100;

        /**
         * 拦截器数据源
         */
        public static final int INTERCEPTOR_DS = -200;
    }

    public static class Number {

        public static final Integer NUMBER_0 = 0;
        public static final Integer NUMBER_1 = 1;
        public static final Integer NUMBER_2 = 2;
        public static final Integer NUMBER_3 = 3;
        public static final Integer NUMBER_4 = 4;
        public static final Integer NUMBER_5 = 5;
        public static final Integer NUMBER_6 = 6;
        public static final Integer NUMBER_7 = 7;
        public static final Integer NUMBER_8 = 8;
        public static final Integer NUMBER_9 = 9;
    }

}

