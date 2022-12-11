package com.dqcer.framework.base.constants;

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
     * feign 前缀
     */
    public static final String FEIGN_PREFIX = "/feign";


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
}

