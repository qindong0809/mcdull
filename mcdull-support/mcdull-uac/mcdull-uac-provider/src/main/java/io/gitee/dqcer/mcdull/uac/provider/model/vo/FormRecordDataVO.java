package io.gitee.dqcer.mcdull.uac.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * FormVO
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class FormRecordDataVO implements VO {

    private Integer id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Map<String, String> itemMap;
}
