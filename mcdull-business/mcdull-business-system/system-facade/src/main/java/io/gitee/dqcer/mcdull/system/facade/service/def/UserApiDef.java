package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User Api Def
 * <p>
 * 单体模式：system-impl 中的本地实现生效，走 JVM 内存调用。
 * 微服务模式：容器无本地实现，自动注册 Feign 代理，走 HTTP 调用。
 *
 * @author dqcer
 * @since 2022/10/28
 */
@RpcClient("mcdull-system")
public interface UserApiDef {

    /**
     * 按 ID 批量查询用户
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/by-id/list")
    Map<Integer, UserVO> getEntityMap(@RequestBody List<Integer> userIdList);

    /**
     * 按 ID 批量查询用户名称映射
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/name-map")
    Map<Integer, String> getNameMap(@RequestBody List<Integer> userIdList);

    /**
     * 按实体列表提取创建人/更新人名称
     * <p>
     * 入参是各业务模块自己的实体集合，远程模式下无法还原实体类型，
     * 因此该方法只保证本地直调语义；契约上仍保留映射注解，避免 Feign 代理构建失败。
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/map-by-entity")
    Map<Integer, String> getMap(@RequestBody Collection<?> entityList);

    /**
     * 按 ID 查询单个用户
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/{userId}")
    UserVO get(@PathVariable("userId") Integer userId);

    /**
     * 按部门 ID 查询下级用户 ID
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/list-by-dept/{departmentId}")
    List<Integer> getUserIdList(@PathVariable("departmentId") Integer departmentId);

    /**
     * 负责人下拉选项
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/responsible/list")
    List<LabelValueVO<Integer, String>> getResponsibleList();

    /**
     * 分页查询用户
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/page")
    PagedVO<UserVO> listByPage(@RequestBody UserListDTO dto);

}
