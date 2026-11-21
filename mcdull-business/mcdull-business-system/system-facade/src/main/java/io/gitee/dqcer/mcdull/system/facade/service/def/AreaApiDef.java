package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 区域数据对外契约（涉及 system 区域表，远程调用）
 */
public interface AreaApiDef {

    /**
     * 批量查询区域编码对应的名称
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/area/name-map")
    Map<String, String> nameMap(@RequestBody Collection<String> codeList);

    /**
     * 省份下拉选项
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/area/province-list")
    List<LabelValueVO<String, String>> provinceList();
}
