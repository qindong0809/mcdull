package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.bean.BeanUtil;
import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.model.dto.DepartmentInfoDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 部门能力实现
 * <p>
 * {@code @Primary}：单体模式下优先注入本地实现。
 * {@code @RestController}：微服务模式下暴露为 HTTP 接口供 Feign 调用。
 */
@Primary
@RestController
public class DeptApiDefImpl implements DeptApiDef {

    @Resource
    private IDepartmentService departmentService;

    @Override
    public List<DepartmentInfoDTO> getAll() {
        return BeanUtil.copyToList(departmentService.getAll(), DepartmentInfoDTO.class);
    }

    @Override
    public Map<Integer, String> getNameMap(List<Integer> idList) {
        return departmentService.getNameMap(idList);
    }
}
