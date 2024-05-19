package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * sys_change_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_notice")
public class NoticeEntity extends BaseEntity<Integer> {

    /**
     * 类型1公告 2动态
     */
    private Integer noticeTypeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否全部可见
     */
    private Boolean allVisibleFlag;
    /**
     * 是否定时发布
     */
    private Boolean scheduledPublishFlag;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 文本内容
     */
    private String contentText;
    /**
     * html内容
     */
    private String contentHtml;
    /**
     * 附件
     */
    private String attachment;
    /**
     * 页面浏览量pv
     */
    private Integer pageViewCount;
    /**
     * 用户浏览量uv
     */
    private Integer userViewCount;
    /**
     * 来源
     */
    private String source;
    /**
     * 作者
     */
    private String author;
    /**
     * 文号
     */
    private String documentNumber;
}