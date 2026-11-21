package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.facade.service.AreaApi;
import io.gitee.dqcer.mcdull.system.provider.web.service.IAreaService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 区域能力实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class AreaApiDefImpl implements AreaApi {

    @Resource
    private IAreaService areaService;

    @Override
    public Map<String, String> nameMap(Collection<String> codeList) {
        return areaService.nameMap(new HashSet<>(codeList));
    }

    @Override
    public List<LabelValueVO<String, String>> provinceList() {
        return areaService.provinceList();
    }
}
