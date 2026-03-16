package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.SerialNumberBusinessTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.SerialNumberFormatTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.SerialNumberVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.SerialNumberMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberRecordService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Serial Number Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SerialNumberServiceImpl
        extends BasicCurdServiceImpl<SerialNumberMapper, SerialNumberEntity> implements ISerialNumberService {

    @Resource
    private ISerialNumberRecordService serialNumberRecordService;

    @Override
    public List<SerialNumberVO> getAll() {
        List<SerialNumberVO> voList = new ArrayList<>();
        List<SerialNumberEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            for (SerialNumberEntity entity : list) {
                SerialNumberVO vo = this.convertToVO(entity);
                Integer businessType = entity.getBusinessType();
                IEnum<Integer> typeEnum = IEnum.getByCode(SerialNumberBusinessTypeEnum.class, businessType);
                if (ObjUtil.isNotEmpty(typeEnum)) {
                    vo.setBusinessName(typeEnum.getText());
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> generate(SerialNumberGenerateDTO dto) {
        SerialNumberEntity entity = super.mustGet(dto.getSerialNumberId());
        Integer count = dto.getCount();
        String format = entity.getFormat();
        Date now = UserContextHolder.getSession().getNow();
        if (StrUtil.contains(format, SerialNumberFormatTypeEnum.YYYY.getCode())) {
            int year = DateUtil.year(now);
            format = StrUtil.replace(format, SerialNumberFormatTypeEnum.YYYY.getCode(), Convert.toStr(year));
        }
        if (StrUtil.contains(format, SerialNumberFormatTypeEnum.MM.getCode())) {
            int month = DateUtil.month(now);
            format = StrUtil.replace(format, SerialNumberFormatTypeEnum.MM.getCode(), Convert.toStr(month));
        }
        if (StrUtil.contains(format, SerialNumberFormatTypeEnum.DD.getCode())) {
            int dayOfMonth = DateUtil.dayOfMonth(now);
            String day = dayOfMonth < 10 ? StrUtil.format("0{}", dayOfMonth) : Convert.toStr(dayOfMonth);
            format = StrUtil.replace(format, SerialNumberFormatTypeEnum.DD.getCode(), day);
        }
        List<SerialNumberRecordEntity> list = serialNumberRecordService.getListBySerialNumber(entity.getId());
        List<Integer> resultList = new ArrayList<>();
        List<String> formatList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int startIndex = format.indexOf("{n");
            int endIndex = format.indexOf("n}");
            if (startIndex > -1 && endIndex > -1 && startIndex < endIndex) {
                String numberFormat = format.substring(startIndex + 1, endIndex + 1);
                Integer lastNumber = 0;
                if (CollUtil.isNotEmpty(list)) {
                    SerialNumberRecordEntity numberRecord = list.get(0);
                    lastNumber = numberRecord.getLastNumber();
                }
                Integer stepRandomRange = entity.getStepRandomRange();
                int resultNumber = lastNumber + stepRandomRange;
                resultList.add(resultNumber);
                String pre = StrUtil.padPre(Convert.toStr(resultNumber), endIndex - startIndex, "0");
                String oneFormat = StrUtil.replace(format, StrUtil.format("{{}}", numberFormat), pre);
                formatList.add(oneFormat);
            }
        }
        if (CollUtil.isNotEmpty(resultList)) {
            if (CollUtil.isNotEmpty(list)) {
                serialNumberRecordService.batchSave(list.get(0), resultList);
            } else {
                serialNumberRecordService.batchSave(entity, resultList);
            }

        }
        return formatList;
    }

    private SerialNumberVO convertToVO(SerialNumberEntity entity) {
        SerialNumberVO serialNumberVO = new SerialNumberVO();
        serialNumberVO.setSerialNumberId(entity.getId());
        serialNumberVO.setBusinessType(entity.getBusinessType());
        serialNumberVO.setFormat(entity.getFormat());
        serialNumberVO.setStepRandomRange(entity.getStepRandomRange());
        serialNumberVO.setRemark(entity.getRemark());
        return serialNumberVO;
    }


    public List<SerialNumberEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<SerialNumberEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SerialNumberEntity::getId, idList);
        return baseMapper.selectList(wrapper);
    }


    public Page<SerialNumberEntity> selectPage(ChangeLogQueryDTO param) {
        LambdaQueryWrapper<SerialNumberEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    public SerialNumberEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    public void insert(SerialNumberEntity entity) {
        baseMapper.insert(entity);
    }

    public boolean exist(SerialNumberEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}
