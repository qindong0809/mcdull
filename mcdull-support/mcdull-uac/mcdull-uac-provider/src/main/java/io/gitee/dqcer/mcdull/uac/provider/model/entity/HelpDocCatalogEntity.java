package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_help_doc_catalog
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_help_doc_catalog")
public class HelpDocCatalogEntity extends RelEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer sort;

    private Long parentId;
}
