package com.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.page.PageUtil;
import com.dqcer.framework.base.page.Paged;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.convert.DictConvert;
import com.dqcer.mcdull.mdc.api.dto.DictFeignDTO;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.DictEntity;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sys dict服务
 *
 * @author dqcer
 * @version  2022/11/08
 */
@Service
public class DictService {

    @Resource
    private IDictRepository dictRepository;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    public Result<List<DictVO>> list(DictFeignDTO dto) {
        LambdaQueryWrapper<DictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictEntity::getSelectType, dto.getSelectType());
        wrapper.eq(DictEntity::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.eq(DictEntity::getLanguage, dto.getLanguage());
        wrapper.orderByAsc(DictEntity::getSort);
        return Result.ok(DictConvert.entitiesConvertToList(dictRepository.list(wrapper)));
    }

    /**
     * 一个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictVO}>
     */
    public Result<DictVO> one(DictFeignDTO dto) {
        LambdaQueryWrapper<DictEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictEntity::getCode, dto.getCode());
        wrapper.eq(DictEntity::getLanguage, dto.getLanguage());
        wrapper.eq(DictEntity::getSelectType, dto.getSelectType());
        wrapper.eq(DictEntity::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.last(SysConstants.LAST_SQL_LIMIT_1);
        return Result.ok(DictConvert.entityConvertToVo(dictRepository.getOne(wrapper)));
    }

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    public Result<Paged<DictVO>> listByPage(DictLiteDTO dto) {
        Page<DictEntity> entityPage = dictRepository.selectPage(dto);

        List<DictVO> voList = new ArrayList<>();
        for (DictEntity entity : entityPage.getRecords()) {
            voList.add(DictConvert.entityConvertToVo(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
