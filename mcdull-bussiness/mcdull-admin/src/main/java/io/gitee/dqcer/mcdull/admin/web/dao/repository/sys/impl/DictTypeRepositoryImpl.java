package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.DictTypeMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictTypeRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* 字典数据 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class DictTypeRepositoryImpl extends ServiceImpl<DictTypeMapper, DictTypeEntity>  implements IDictTypeRepository {


    @Override
    public Page<DictTypeEntity> selectPage(DictTypeLiteDTO dto) {
        LambdaQueryWrapper<DictTypeEntity> lambda = new QueryWrapper<DictTypeEntity>().lambda();
        String dictType = dto.getDictType();
        if (StrUtil.isNotBlank(dictType)) {
            lambda.like(DictTypeEntity::getDictType, dictType);
        }
        String dictName = dto.getDictName();
        if (StrUtil.isNotBlank(dictName)) {
            lambda.like(DictTypeEntity::getDictName, dictName);
        }
        String status = dto.getStatus();
        if (ObjUtil.isNotNull(status)) {
            lambda.eq(DictTypeEntity::getStatus, status);
        }
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            lambda.between(RelEntity::getCreatedTime, startTime, endTime);
        }
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lambda);
    }

    @Override
    public List<DictTypeEntity> getListByName(String dictType) {
        LambdaQueryWrapper<DictTypeEntity> query = Wrappers.lambdaQuery();
        query.eq(DictTypeEntity::getDictType, dictType);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdateById(Long id) {
        baseMapper.deleteById(id);
    }
}