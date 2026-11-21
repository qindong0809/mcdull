package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.json.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ExportContextVO;
import io.gitee.dqcer.mcdull.system.facade.service.ExportApi;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出上下文实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class ExportApiDefImpl implements ExportApi {

    @Resource
    private IUserService userService;
    @Resource
    private IMenuService menuService;
    @Resource
    private ICommonManager commonManager;

    @Override
    public ExportContextVO context() {
        ExportContextVO vo = new ExportContextVO();
        Integer userId = UserContextHolder.userId();
        if (userId != null) {
            Map<Integer, String> nameMap = userService.getNameMap(List.of(userId));
            vo.setOperatorName(nameMap.get(userId));
        }
        try {
            List<String> menuName = menuService.getCurrentMenuName();
            vo.setMenuName(CollUtil.isNotEmpty(menuName) ? StrUtil.join("_", menuName) : StrUtil.EMPTY);
        } catch (Exception e) {
            // 远程导出调用可能未透传 permissionCode，菜单名仅用于拼文件名，取不到时降级为空
            vo.setMenuName(StrUtil.EMPTY);
        }
        vo.setAutoStorage(commonManager.getConfigToBool("export-auto-storage"));
        vo.setFileDomainName(commonManager.getConfig("file-domain-name"));
        vo.setDateTimeStr(TimeZoneUtil.serializeDate(new Date(), null, null, null, null, false));
        return vo;
    }
}
