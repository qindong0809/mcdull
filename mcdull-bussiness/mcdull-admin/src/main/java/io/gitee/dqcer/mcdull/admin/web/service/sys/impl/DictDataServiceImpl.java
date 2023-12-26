package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IDictTransformerService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.DictDataConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictDataRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictDataService;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层
 *
 * @author dqcer
 * @since  2023/03/18
 */
@Service
public class DictDataServiceImpl extends BasicServiceImpl<IDictDataRepository> implements IDictDataService, IDictTransformerService {


    @Override
    public Result<List<DictDataVO>> dictType(String dictType) {
        List<DictDataVO> voList = new ArrayList<>();
        List<DictDataDO> list = baseRepository.dictType(dictType);
        for (DictDataDO dictDataDO : list) {
            voList.add(DictDataConvert.convertToDictDataVO(dictDataDO));
        }
        return Result.success(voList);
    }

    @Override
    public Result<PagedVO<DictDataVO>> list(DictDataLiteDTO dto) {

        List<DictDataVO> voList = new ArrayList<>();

        Page<DictDataDO> entityPage = baseRepository.selectPage(dto);
        for (DictDataDO entity : entityPage.getRecords()) {
            voList.add(DictDataConvert.convertToDictDataVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 翻译
     *
     * @param code       代码
     * @param selectType 选择类型
     * @param language   语言
     * @return {@link KeyValueVO}
     */
    @Override
    public KeyValueBO<String, String> transformer(String code, String selectType, String language) {
        List<DictDataDO> doList = baseRepository.dictType(selectType);
        if (CollUtil.isNotEmpty(doList)) {
            DictDataDO first = doList.stream().filter(i -> i.getDictValue().equals(code)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(first)) {
                return new KeyValueBO<String, String>().setKey(first.getDictValue()).setValue(first.getDictLabel());
            }
        }
        return new KeyValueBO<>();
    }

    @Override
    public Result<DictDataVO> detail(Long dictCode) {
        DictDataDO dataDO = baseRepository.getById(dictCode);
        return Result.success(DictDataConvert.convertToDictDataVO(dataDO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(DictDataAddDTO dto) {
        List<DictDataDO> list = baseRepository.getNameList(dto.getDictType(), dto.getDictValue());
        this.validNameExist(null, dto.getDictValue(), list);
        DictDataDO dataDO = DictDataConvert.convertToDictDataDo(dto);
        baseRepository.save(dataDO);
        return Result.success(dataDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(DictDataEditDTO dto) {
        Long dictCode = dto.getDictCode();
        List<DictDataDO> list = baseRepository.getNameList(dto.getDictType(), dto.getDictValue());
        this.validNameExist(dictCode, dto.getDictValue(), list);
        DictDataDO dataDO = DictDataConvert.convertToDictDataDo(dto);
        dataDO.setId(dictCode);
        baseRepository.updateById(dataDO);
        return Result.success(dictCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.success(id);
    }
}
