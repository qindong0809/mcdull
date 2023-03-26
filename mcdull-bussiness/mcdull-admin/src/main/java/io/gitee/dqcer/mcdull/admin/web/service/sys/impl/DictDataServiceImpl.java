package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IDictTransformerService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.DictDataConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictDataRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictDataService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层
 *
 * @author dqcer
 * @since  2023/03/18
 */
@Service
public class DictDataServiceImpl implements IDictDataService, IDictTransformerService {

    @Resource
    private IDictDataRepository dictDataRepository;

    @Override
    public Result<List<DictDataVO>> dictType(String dictType) {
        List<DictDataVO> voList = new ArrayList<>();
        List<DictDataDO> list = dictDataRepository.dictType(dictType);
        for (DictDataDO dictDataDO : list) {
            voList.add(DictDataConvert.convertToDictDataVO(dictDataDO));
        }
        return Result.ok(voList);
    }

    @Override
    public Result<PagedVO<DictDataVO>> list(DictDataLiteDTO dto) {

        List<DictDataVO> voList = new ArrayList<>();

        Page<DictDataDO> entityPage = dictDataRepository.selectPage(dto);
        for (DictDataDO entity : entityPage.getRecords()) {
            voList.add(DictDataConvert.convertToDictDataVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
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
    public KeyValueVO<String, String> transformer(String code, String selectType, String language) {
        List<DictDataDO> doList = dictDataRepository.dictType(selectType);
        if (CollUtil.isNotEmpty(doList)) {
            DictDataDO first = doList.stream().filter(i -> i.getDictValue().equals(code)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(first)) {
                return new KeyValueVO<String, String>().setId(first.getDictValue()).setName(first.getDictLabel());
            }
        }
        return new KeyValueVO<>();
    }

    @Override
    public Result<DictDataVO> detail(Long dictCode) {
        DictDataDO dataDO = dictDataRepository.getById(dictCode);
        return Result.ok(DictDataConvert.convertToDictDataVO(dataDO));
    }
}
