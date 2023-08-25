package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 系统配置key枚举
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum SysConfigKeyEnum implements IEnum<String> {
    /**
     * 登录是否开启验证码
     */
    CAPTCHA("sys.account.captchaEnabled", "登录是否开启验证码"),

    /**
     * 文件目录
     */
    FILE_DIRECTORY("sys.upload.file.directory", "上传文件目录"),

    CREATED_USER_SEND_EMAIL("sys.user.add.sendEmail", "添加用户邮件通知"),


    DATABASE_SQL_DUMP("database.sql.dump.directory", "sql备份文件目录"),

    ;

    SysConfigKeyEnum(String code, String text) {
        init(code, text);
    }

}
