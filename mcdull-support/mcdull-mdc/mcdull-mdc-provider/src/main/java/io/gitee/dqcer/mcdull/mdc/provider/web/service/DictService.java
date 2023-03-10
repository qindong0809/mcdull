package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.DictConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.DictLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class DictService {

    @Resource
    private IDictRepository dictRepository;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    public Result<List<DictClientVO>> list(DictClientDTO dto) {
        LambdaQueryWrapper<DictDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictDO::getSelectType, dto.getSelectType());
        wrapper.eq(DictDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.eq(DictDO::getLanguage, dto.getLanguage());
        wrapper.orderByAsc(DictDO::getSort);
        return Result.ok(DictConvert.entitiesConvertToList(dictRepository.list(wrapper)));
    }

    /**
     * 一个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictClientVO}>
     */
    public Result<DictClientVO> one(DictClientDTO dto) {
        LambdaQueryWrapper<DictDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictDO::getCode, dto.getCode());
        wrapper.eq(DictDO::getLanguage, dto.getLanguage());
        wrapper.eq(DictDO::getSelectType, dto.getSelectType());
        wrapper.eq(DictDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.last(GlobalConstant.Database.SQL_LIMIT_1);
        return Result.ok(DictConvert.entityConvertToVo(dictRepository.getOne(wrapper)));
    }

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    public Result<PagedVO<DictClientVO>> listByPage(DictLiteDTO dto) {
        Page<DictDO> entityPage = dictRepository.selectPage(dto);

        List<DictClientVO> voList = new ArrayList<>();
        for (DictDO entity : entityPage.getRecords()) {
            voList.add(DictConvert.entityConvertToVo(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
