package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;

import java.util.Date;

/**
 * 通知公告表 实体类
 *
 * @author dqcer
 * @version 2023-01-18
 */
@TableName("sys_notice")
public class NoticeDO extends BaseDO {

    private static final long serialVersionUID = 1L;

   /**
    * 公告标题
    */
    private String title;

   /**
    * 公告内容
    */
    private String content;

   /**
    * 公告类型（1通知 2公告）
    */
    private Integer type;

   /**
    * 状态（1/正常 2/停用）
    */
    private Integer status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SysNotice{" +
            ", title=" + title +
            ", content=" + content +
            ", type=" + type +
        "}";
    }
}
