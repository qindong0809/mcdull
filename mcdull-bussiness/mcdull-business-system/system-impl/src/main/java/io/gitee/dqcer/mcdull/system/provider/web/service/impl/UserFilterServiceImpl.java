package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserFilterDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserFilterEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserFilterVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.UserFilterMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserFilterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User Filter Service
 *
 * @author dqcer
 * @since 2026/02/09
 */
@Service
public class UserFilterServiceImpl extends BasicCurdServiceImpl<UserFilterMapper, UserFilterEntity> implements IUserFilterService {

    @Override
    public List<UserFilterVO> getList(Integer userId, Integer roleId, String categoryCode, String subCategoryCode) {
        LambdaQueryWrapper<UserFilterEntity> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(UserFilterEntity::getUserId, userId).eq(UserFilterEntity::getRoleId, roleId)
                .eq(UserFilterEntity::getCategoryCode, categoryCode)
            .eq(CharSequenceUtil.isNotBlank(subCategoryCode), UserFilterEntity::getSubCategoryCode, subCategoryCode);
        lambdaQuery.orderByDesc(UserFilterEntity::getId);
        List<UserFilterEntity> list = baseMapper.selectList(lambdaQuery);
        return this.convertVoList(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id) {
        UserFilterEntity entity = super.getById(id);
        if (entity == null) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        return super.removeById(id);
    }

    @Override
    public boolean save(Integer userId, Integer roleId, UserFilterDTO dto) {
        List<UserFilterVO> list = this.getList(userId, roleId, dto.getCategoryCode(), dto.getSubCategoryCode());
        boolean existTitle = list.stream().anyMatch(item -> CharSequenceUtil.equalsIgnoreCase(item.getFilterTitle(), dto.getFilterTitle()));
        if (existTitle) {
            LogicCheckUtil.throwDataExistException(dto.getFilterTitle());
        }
        if (CollUtil.isNotEmpty(list)) {
            List<UserFilterVO> needDelList = list.subList(10 - 1, list.size());
            super.removeByIds(needDelList.stream().map(UserFilterVO::getId).toList());
        }
        UserFilterEntity entity = new UserFilterEntity();
        entity.setUserId(userId);
        entity.setRoleId(roleId);
        entity.setCategoryCode(dto.getCategoryCode());
        entity.setSubCategoryCode(dto.getSubCategoryCode());
        entity.setFilterTitle(dto.getFilterTitle());
        entity.setFilterContent(dto.getFilterContent());
        return super.save(entity);
    }

    private List<UserFilterVO> convertVoList(List<UserFilterEntity> list) {
        List<UserFilterVO> voList = new ArrayList<>();
        for (UserFilterEntity filter : list) {
            UserFilterVO vo = new UserFilterVO();
            vo.setId(filter.getId());
            vo.setUserId(filter.getUserId());
            vo.setRoleId(filter.getRoleId());
            vo.setCategoryCode(filter.getCategoryCode());
            vo.setSubCategoryCode(filter.getSubCategoryCode());
            vo.setFilterTitle(filter.getFilterTitle());
            vo.setFilterContent(filter.getFilterContent());
            voList.add(vo);
        }
        return voList;
    }
}
