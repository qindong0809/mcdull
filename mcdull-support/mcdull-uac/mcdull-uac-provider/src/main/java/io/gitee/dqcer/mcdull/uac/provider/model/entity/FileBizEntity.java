package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_file_biz")
public class FileBizEntity extends RelEntity<Integer> {

    private Integer fileId;

    private Integer bizId;

    private String bizCode;

}
