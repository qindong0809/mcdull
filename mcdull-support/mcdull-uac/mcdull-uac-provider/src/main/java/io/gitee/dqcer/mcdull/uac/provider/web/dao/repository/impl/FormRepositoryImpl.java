package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FormMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRepository;
import org.springframework.stereotype.Service;

/**
 * Form Repository Impl
 *
 * @author dqcer
 * @since 2024/7/25 13:20
 */
@Service
public class FormRepositoryImpl extends
        CrudRepository<FormMapper, FormEntity> implements IFormRepository {

    @Override
    public Page<FormEntity> selectPage(FormQueryDTO param) {
        LambdaQueryWrapper<FormEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(FormEntity::getName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public FormEntity getByName(String name) {
        LambdaQueryWrapper<FormEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(FormEntity::getName, name);
        return baseMapper.selectOne(lambda);
    }

}
