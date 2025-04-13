package io.gitee.dqcer.blaze.domain.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;


@Data
public class ReportViewVO implements VO {

    private List<String> typeName;

    private List<Integer> talentValue;

    private List<Integer>  customerValue;
}
