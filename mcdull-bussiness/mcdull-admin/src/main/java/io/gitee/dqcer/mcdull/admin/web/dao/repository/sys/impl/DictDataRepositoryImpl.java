package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.DictDataMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictDataRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 字典类型 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class DictDataRepositoryImpl extends ServiceImpl<DictDataMapper, DictDataDO>  implements IDictDataRepository {

    @Override
    public List<DictDataDO> dictType(String dictType) {
        LambdaQueryWrapper<DictDataDO> wrapper = Wrappers.lambdaQuery(DictDataDO.class);
        wrapper.eq(DictDataDO::getStatus, StatusEnum.ENABLE.getCode());
        wrapper.eq(DictDataDO::getDictType, dictType);
        wrapper.orderByAsc(DictDataDO::getDictSort);
        List<DictDataDO> list = baseMapper.selectList(wrapper);

        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public Page<DictDataDO> selectPage(DictDataLiteDTO dto) {
        LambdaQueryWrapper<DictDataDO> lambda = new QueryWrapper<DictDataDO>().lambda();
        lambda.like(DictDataDO::getDictType, dto.getDictType());
        String dictLabel = dto.getDictLabel();
        if (StrUtil.isNotBlank(dictLabel)) {
            lambda.like(DictDataDO::getDictLabel, dictLabel);
        }
        Integer status = dto.getStatus();
        if (ObjUtil.isNotNull(status)) {
            lambda.eq(DictDataDO::getStatus, status);
        }
        lambda.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        lambda.orderByAsc(DictDataDO::getDictSort);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}