package io.gitee.dqcer.mcdull.workflow.web.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowTaskDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowHisTaskVO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowTaskVO;
import io.gitee.dqcer.mcdull.workflow.web.mapper.ExecuteMapper;
import io.gitee.dqcer.mcdull.workflow.web.service.IExecuteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.warm.flow.core.entity.User;
import org.dromara.warm.flow.core.enums.UserType;
import org.dromara.warm.flow.core.service.UserService;
import org.dromara.warm.flow.core.utils.StreamUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ExecuteServiceImpl implements IExecuteService {

    @Resource
    private ExecuteMapper executeMapper;
    @Resource
    private UserService flowUserService;
    @Resource
    private IUserService userService;
    @Resource
    private IDepartmentService departmentService;
    @Resource
    private IRoleService roleService;

    @Override
    public PagedVO<FlowTaskVO> toDoPage(FlowTaskDTO dto) {
        // todo set permissionList ?
        dto.setPermissionList(this.permissionList());
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<FlowTaskVO> pagedVO = executeMapper.getTodoList(page, dto);
        List<FlowTaskVO> records = pagedVO.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> list = CollStreamUtil.toList(records, FlowTaskVO::getId);
            List<User> userList = flowUserService.getByAssociateds(list);
            Map<Long, List<User>> map = CollStreamUtil.groupByKey(userList, User::getAssociated);
            for (FlowTaskVO taskVo : records) {
                List<User> users = map.get(taskVo.getId());
                if (CollUtil.isNotEmpty(users)) {
                    for (User user : users) {
                        if (UserType.APPROVAL.getKey().equals(user.getType())) {
                            if (StrUtil.isBlank(taskVo.getApprover())) {
                                taskVo.setApprover("");
                            }
                            String name = this.getName(user.getProcessedBy());
                            if (StrUtil.isNotBlank(name)) {
                                taskVo.setApprover(taskVo.getApprover().concat(name).concat(";"));
                            }
                        } else if (UserType.TRANSFER.getKey().equals(user.getType())) {
                            if (StrUtil.isBlank(taskVo.getTransferredBy())) {
                                taskVo.setTransferredBy("");
                            }
                            String name = this.getName(user.getProcessedBy());
                            if (StrUtil.isNotBlank(name)) {
                                taskVo.setTransferredBy(taskVo.getTransferredBy().concat(name).concat(";"));
                            }
                        } else if (UserType.DEPUTE.getKey().equals(user.getType())) {
                            if (StrUtil.isBlank(taskVo.getDelegate())) {
                                taskVo.setDelegate("");
                            }
                            String name = this.getName(user.getProcessedBy());
                            if (StrUtil.isNotBlank(name)) {
                                taskVo.setDelegate(taskVo.getDelegate().concat(name).concat(";"));
                            }
                        }
                    }
                }
            }
        }
        return PageUtil.toPage(pagedVO);
    }

    @Override
    public PagedVO<FlowHisTaskVO> copyPage(FlowTaskDTO dto) {
        dto.setPermissionList(this.permissionList());
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<FlowHisTaskVO> pageInfo = executeMapper.copyPage(page, dto);
        List<FlowHisTaskVO> records = pageInfo.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Integer> userIdList = CollStreamUtil.toList(records, FlowHisTaskVO::getApproverUserId);
            Map<Integer, String> userMap = userService.getNameMap(userIdList);
            for (FlowHisTaskVO taskVo : records) {
                taskVo.setApprover(userMap.get(taskVo.getApproverUserId()));
            }
        }
        return PageUtil.toPage(pageInfo);
    }

    private List<String> permissionList() {
        String userId = UserContextHolder.userIdStr();
        Map<Integer, List<RoleEntity>> roleMap = roleService.getRoleMap(ListUtil.of(Convert.toInt(userId)));

        List<String> permissionList = new ArrayList<>();
        if (MapUtil.isNotEmpty(roleMap)) {
            List<RoleEntity> entityList = roleMap.get(Convert.toInt(userId));
            if (CollUtil.isNotEmpty(entityList)) {
                permissionList = CollStreamUtil.toList(entityList, r -> "role:" + r.getId());
            }
        }
        permissionList.add(userId);
        UserEntity userEntity = userService.get(Convert.toInt(userId));
        if (Objects.nonNull(userEntity)) {
            permissionList.add("dept:" + userEntity.getDepartmentId());
        }
        LogHelp.info(log, "当前用户权限[{}]", permissionList);
        return permissionList;
    }

    public String getName(String id) {
        UserListDTO userDTO = new UserListDTO();
        List<UserVO> userList = PageUtil.getAllList(userDTO, r -> userService.listByPage(r));
        Map<Integer, String> userMap = StreamUtils.toMap(userList
                , UserVO::getEmployeeId, UserVO::getActualName);
        Map<Integer, String> deptMap = StreamUtils.toMap(departmentService.getAll()
                , DepartmentInfoVO::getDepartmentId, DepartmentInfoVO::getName);
        Map<Integer, String> roleMap = StreamUtils.toMap(roleService.all()
                , RoleVO::getRoleId, RoleVO::getRoleName);
        if (StrUtil.isNotBlank(id)) {
            if (id.contains("user:")) {
                String name = userMap.get(Integer.valueOf(id.replace("user:", "")));
                if (StrUtil.isNotBlank(name)) {
                    return "用户:" + name;
                }
            } else if (id.contains("dept:")) {
                String name = deptMap.get(Integer.valueOf(id.replace("dept:", "")));
                if (StrUtil.isNotBlank(name)) {
                    return "部门:" + name;
                }
            } else if (id.contains("role")) {
                String name = roleMap.get(Integer.valueOf(id.replace("role:", "")));
                if (StrUtil.isNotBlank(name)) {
                    return "角色:" + name;
                }
            } else {
                try {
                    int parseLong = Integer.parseInt(id);
                    String name = userMap.get(parseLong);
                    if (StrUtil.isNotBlank(name)) {
                        return "用户:" + name;
                    }
                } catch (NumberFormatException e) {
                    return id;
                }

            }
        }
        return "";
    }
}
