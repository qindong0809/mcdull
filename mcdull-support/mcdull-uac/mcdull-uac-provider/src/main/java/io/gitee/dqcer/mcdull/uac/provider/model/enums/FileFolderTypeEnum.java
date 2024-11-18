package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import lombok.Getter;

/**
 * 文件服务 文件夹位置类型枚举类
 *
 */
@Getter
public enum FileFolderTypeEnum implements IEnum<Integer> {

    /**
     * 通用
     */
    COMMON(1, FileFolderTypeEnum.FOLDER_PUBLIC + "/common/", "通用"),

    /**
     * 公告
     */
    NOTICE(2, FileFolderTypeEnum.FOLDER_PUBLIC + "/notice/", "公告"),

    /**
     * 帮助中心
     */
    HELP_DOC(3, FileFolderTypeEnum.FOLDER_PUBLIC + "/help-doc/", "帮助中心"),

    /**
     * 意见反馈
     */
    FEEDBACK(4, FileFolderTypeEnum.FOLDER_PUBLIC + "/feedback/", "意见反馈"),

    /**
     * 导出
     */
    EXPORT(5, FileFolderTypeEnum.FOLDER_PUBLIC + "/export/", "导出"),

    SYSTEM_DATABASE_BACKUP(9999, FileFolderTypeEnum.FOLDER_PRIVATE + "/system/database-backup/", "系统/数据库备份"),

    ;

    /**
     * 公用读取文件夹 public
     */
    public static final String FOLDER_PUBLIC = "public";

    /**
     * 私有读取文件夹 private， 私有文件夹会设置 只读权限，并且 文件url 拥有过期时间
     */
    public static final String FOLDER_PRIVATE = "private";

    private final Integer value;

    private final String folder;

    private final String desc;

    FileFolderTypeEnum(Integer value, String folder, String desc) {
        init(value, folder);
        this.desc = desc;
        this.folder = folder;
        this.value = value;
    }
}

