package com.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.convert.SysDictConvert;
import com.dqcer.mcdull.mdc.api.dto.SysDictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.SysDictEntity;
import com.dqcer.mcdull.mdc.api.vo.SysDictVO;
import com.dqcer.mcdull.mdc.provider.web.dao.SysDictDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * sys dict服务
 *
 * @author dqcer
 * @version  2022/11/08
 */
@Service
public class SysDictService {

    @Resource
    private SysDictDAO sysDictDAO;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link SysDictVO}>>
     */
    public Result<List<SysDictVO>> list(SysDictLiteDTO dto) {
        LambdaQueryWrapper<SysDictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysDictEntity::getSelectType, dto.getSelectType());
        wrapper.ge(SysDictEntity::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.orderByAsc(SysDictEntity::getSort);
        return Result.ok(SysDictConvert.entitiesConvertToList(sysDictDAO.selectList(wrapper)));
    }

    /**
     * 一个
     *
     * @param dto dto
     * @return {@link Result}<{@link SysDictVO}>
     */
    public Result<SysDictVO> one(SysDictLiteDTO dto) {
        LambdaQueryWrapper<SysDictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysDictEntity::getCode, dto.getCode());
        wrapper.ge(SysDictEntity::getLanguage, dto.getLanguage());
        wrapper.ge(SysDictEntity::getSelectType, dto.getSelectType());
        wrapper.ge(SysDictEntity::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.last(SysConstants.LAST_SQL_LIMIT_1);
        return Result.ok(SysDictConvert.entityConvertToVo(sysDictDAO.selectOne(wrapper)));
    }
}
