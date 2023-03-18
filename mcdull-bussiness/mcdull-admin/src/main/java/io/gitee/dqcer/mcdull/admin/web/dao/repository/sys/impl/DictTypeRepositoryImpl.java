package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.DictTypeMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictTypeRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* 字典数据 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class DictTypeRepositoryImpl extends ServiceImpl<DictTypeMapper, DictTypeDO>  implements IDictTypeRepository {


    @Override
    public Page<DictTypeDO> selectPage(DictTypeLiteDTO dto) {
        LambdaQueryWrapper<DictTypeDO> lambda = new QueryWrapper<DictTypeDO>().lambda();
        String dictType = dto.getDictType();
        if (StrUtil.isNotBlank(dictType)) {
            lambda.like(DictTypeDO::getDictType, dictType);
        }
        String dictName = dto.getDictName();
        if (StrUtil.isNotBlank(dictName)) {
            lambda.like(DictTypeDO::getDictName, dictName);
        }
        Integer status = dto.getStatus();
        if (ObjUtil.isNotNull(status)) {
            lambda.eq(DictTypeDO::getStatus, status);
        }
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            lambda.between(MiddleDO::getCreatedTime, startTime, endTime);
        }
        lambda.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}