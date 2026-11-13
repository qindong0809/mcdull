package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * 系统更新日志 列表VO
 *
 */

@Data
public class ChangeLogAndVersionVO implements VO {

    private List<ChangeLogVO> list;

    private Properties gitVersion;

    private Properties jarVersion;

}