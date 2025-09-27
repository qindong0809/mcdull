package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminUserMenuMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminUserMenuServiceImpl extends ServiceImpl<AdminUserMenuMapper, AdminUserMenuEntity> implements IAdminUserMenuService {


    @Override
    public Map<Integer, List<Integer>> getUserMenu() {
        LambdaQueryWrapper<AdminUserMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<AdminUserMenuEntity> list = baseMapper.selectList(queryWrapper);
        return list.stream().collect(
                Collectors.groupingBy(AdminUserMenuEntity::getUserId,
                        Collectors.mapping(AdminUserMenuEntity::getMenuId, Collectors.toList()))
        );
    }
}
