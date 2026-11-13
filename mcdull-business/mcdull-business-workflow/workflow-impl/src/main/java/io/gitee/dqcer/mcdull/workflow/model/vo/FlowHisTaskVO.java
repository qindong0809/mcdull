package io.gitee.dqcer.mcdull.workflow.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowHisTask;

@EqualsAndHashCode(callSuper = true)
@Data
public class FlowHisTaskVO extends FlowHisTask implements VO {

    private Integer approverUserId;
}
