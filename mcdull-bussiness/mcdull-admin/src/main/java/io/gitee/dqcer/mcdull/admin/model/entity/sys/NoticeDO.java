package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import lombok.Data;

/**
 * 通知公告表 实体类
 *
 * @author dqcer
 * @since 2023-01-18
 */
@TableName("sys_notice")
@Data
public class NoticeDO extends BaseDO {

    private static final long serialVersionUID = 1L;

   /**
    * 公告标题
    */
    private String noticeTitle;

   /**
    * 公告内容
    */
    private String noticeContent;

   /**
    * 公告类型（1通知 2公告）
    */
    private String noticeType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;
}
