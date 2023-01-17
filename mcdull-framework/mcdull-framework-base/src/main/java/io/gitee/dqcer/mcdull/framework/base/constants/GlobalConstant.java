package io.gitee.dqcer.mcdull.framework.base.constants;

/**
 * 全局常量
 *
 * @author dqcer
 * @version 2022/10/26
 */
@SuppressWarnings("unused")
public class GlobalConstant {

    public static final String ROOT_PREFIX = "mcdull";

    /**
     * 内部接口调用 前缀
     */
    public static final String INNER_API = "/interior-api";



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

