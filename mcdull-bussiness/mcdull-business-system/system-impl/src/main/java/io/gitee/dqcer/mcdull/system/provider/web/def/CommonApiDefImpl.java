package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.facade.service.def.CommonApiDef;
import io.gitee.dqcer.mcdull.system.facade.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.facade.model.enums.DictSelectTypeEnum;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IArea;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IFileVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IAreaService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictValueService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 公共业务能力本地实现
 */
@Primary
@Service
public class CommonApiDefImpl implements CommonApiDef {

    @Resource
    private ICommonManager commonManager;
    @Resource
    private IAreaService areaService;
    @Resource
    private ISerialNumberService serialNumberService;
    @Resource
    private IDictValueService dictValueService;

    @Override
    public String convertDateTimeStr(Date date) {
        return commonManager.convertDateTimeStr(date);
    }

    @Override
    public String convertDateByUserTimezone(Date date) {
        return commonManager.convertDateByUserTimezone(date);
    }

    @Override
    public String getConfig(String key) {
        return commonManager.getConfig(key);
    }

    @Override
    public Boolean getConfigToBool(String key) {
        return commonManager.getConfigToBool(key);
    }

    @Override
    public void downloadExcelTemplate(Map<String, List<DynamicFieldBO>> sheetMap, String fileNamePrefix) {
        commonManager.downloadExcelTemplate(sheetMap, fileNamePrefix);
    }

    @Override
    public <D extends PagedDTO, V extends VO> void exportExcel(D dto, Function<D, PagedVO<V>> function,
                                                               String sheetName, List<Pair<String, Func1<V, ?>>> titlePairList) {
        commonManager.exportExcel(dto, function, sheetName, titlePairList);
    }

    @Override
    public <V extends VO> void exportExcel(List<V> list, String sheetName, List<Pair<String, Func1<V, ?>>> titleFuncList) {
        commonManager.exportExcel(list, sheetName, titleFuncList);
    }

    @Override
    public void setFileVO(List<? extends IFileVO> voList, Class<?> clazz) {
        commonManager.setFileVO(voList, clazz);
    }

    @Override
    public List<Pair<String, byte[]>> getFileDateList(Integer bizId, Class<?> clazz) {
        return commonManager.getFileDateList(bizId, clazz);
    }

    @Override
    public void setDepartment(List<? extends ApproveVO> voList, Integer userId) {
        commonManager.setDepartment(voList, userId);
    }

    @Override
    public void set(List<? extends IArea> voList) {
        areaService.set(voList);
    }

    @Override
    public List<LabelValueVO<String, String>> provinceList() {
        return areaService.provinceList();
    }

    @Override
    public KeyValueBO<String, String> dictVO(DictSelectTypeEnum selectType, String value) {
        return dictValueService.dictVO(selectType, value);
    }

    @Override
    public List<String> generate(SerialNumberGenerateDTO dto) {
        return serialNumberService.generate(dto);
    }
}
