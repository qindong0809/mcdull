package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.DictValueMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictValueRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class DicctValueRepositoryImpl extends ServiceImpl<DictValueMapper, DictValueEntity> implements IDictValueRepository {



    @Override
    public void insert(DictValueEntity entity) {
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
    }


    @Override
    public List<DictValueEntity> getListByDictKeyId(Long dictKeyId) {
        if (ObjUtil.isNotNull(dictKeyId)) {
            LambdaQueryWrapper<DictValueEntity> wrapper = Wrappers.lambdaQuery(DictValueEntity.class)
                    .eq(DictValueEntity::getDictKeyId, dictKeyId);
            return baseMapper.selectList(wrapper);
        }
        return Collections.emptyList();
    }

    @Override
    public Page<DictValueEntity> selectPage(DictValueQueryDTO dto) {
        LambdaQueryWrapper<DictValueEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getSearchWord();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictValueEntity::getValueName, keyword)
                    .or().like(DictValueEntity::getValueCode, keyword)
            );
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public void insert(DictValueAddDTO dto) {
        DictValueEntity entity = new DictValueEntity();
        entity.setDictKeyId(dto.getDictKeyId());
        entity.setValueCode(dto.getValueCode());
        entity.setValueName(dto.getValueName());
        entity.setSort(dto.getSort());
        entity.setRemark(dto.getRemark());
        this.insert(entity);
    }
}
