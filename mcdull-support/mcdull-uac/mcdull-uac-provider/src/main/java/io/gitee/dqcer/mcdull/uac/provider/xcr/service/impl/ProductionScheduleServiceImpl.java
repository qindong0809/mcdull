package io.gitee.dqcer.mcdull.uac.provider.xcr.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.util.ExcelUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICommonService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IProductionScheduleRepository;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.entity.ProductionScheduleEntity;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.vo.ProductionScheduleVO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.service.ProductionScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 生产进度表 Service
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */

@Service
public class ProductionScheduleServiceImpl
        extends BasicServiceImpl<IProductionScheduleRepository> implements ProductionScheduleService {

    @Resource
    private IUserService userService;

    @Resource
    private ICommonService commonService;

    public PagedVO<ProductionScheduleVO> queryPage(ProductionScheduleQueryDTO dto) {
        List<ProductionScheduleVO> voList = new ArrayList<>();
        Page<ProductionScheduleEntity> entityPage = baseRepository.selectPage(dto);
        List<ProductionScheduleEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Integer userId = UserContextHolder.userId();
            for (ProductionScheduleEntity entity : recordList) {
                ProductionScheduleVO vo = this.convertToVO(entity);
                vo.setEditAble(ObjUtil.equal(userId, entity.getOwner()));
                voList.add(vo);
            }
        }
        this. extracted(voList);
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public void exportData(ProductionScheduleQueryDTO dto) {
        Integer userId = UserContextHolder.userId();
        String actualName = userService.getActualName(userId);
        String s = TimeZoneUtil.serializeDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("客户 + 地址", "customerAddress");
        titleMap.put("产品名称", "productName");
        titleMap.put("测量", "measurementDateBy");
        titleMap.put("制图", "drawingDateBy");
        titleMap.put("客户确认日期", "customerConfirmationDate");
        titleMap.put("拆单", "orderBreakingDateBy");
        titleMap.put("下生产单", "productionOrderDate");
        titleMap.put("工序", "processDateDescription");
        titleMap.put("出货", "deliveryDateBy");
        titleMap.put("安装", "installationDateBy");
        titleMap.put("约定生产周期", "productionCycleRemaining");
        titleMap.put("备注", "remark");

        List<Map<String, String>> allRecord = new ArrayList<>();
        dto.setPageSize(Integer.MAX_VALUE);
        PagedVO<ProductionScheduleVO> page = this.queryPage(dto);
        List<ProductionScheduleVO> list = page.getList();
        if (CollUtil.isEmpty(list)) {
            list = new ArrayList<>();
        }
        for (ProductionScheduleVO vo : list) {
            Map<String, String> map = new HashMap<>();
            map.put("customerAddress", vo.getAddressCustomer());
            map.put("productName", vo.getProductName());
            map.put("measurementDateBy", vo.getMeasurement());
            map.put("drawingDateBy", vo.getDrawing());
            map.put("customerConfirmationDate", vo.getCustomerConfirmation());
            map.put("orderBreakingDateBy", vo.getOrderBreaking());
            map.put("productionOrderDate", vo.getProductionOrder());
            map.put("processDateDescription", vo.getProcess());
            map.put("deliveryDateBy", vo.getDelivery());
            map.put("installationDateBy", vo.getInstallation());
            map.put("productionCycleRemaining", vo.getProductionCycleRemaining().toString());
            map.put("remark", vo.getRemark());
            allRecord.add(map);
        }

        ExcelUtil.exportExcelByMap(outputStream, "生产进度",
                this.filterConditionsStr(dto), actualName, s, titleMap, allRecord);
        byte[] byteArray = outputStream.toByteArray();

        String fileName = commonService.getFileName(FileExtensionTypeEnum.EXCEL_X, "生产进度");

        HttpServletResponse response = ServletUtil.getResponse();
        ServletUtil.setDownloadFileHeader(response, fileName, (long) byteArray.length);
        try {
            response.getOutputStream().write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void extracted(List<ProductionScheduleVO> list) {
        Set<Integer> userSet = new HashSet<>();
        for (ProductionScheduleVO vo : list) {
            Integer measurementBy = vo.getMeasurementBy();
            if (ObjUtil.isNotNull(measurementBy)) {
                userSet.add(measurementBy);
            }
            Integer drawingBy = vo.getDrawingBy();
            if (ObjUtil.isNotNull(drawingBy)) {
                userSet.add(drawingBy);
            }
            Integer orderBreakingBy = vo.getOrderBreakingBy();
            if (ObjUtil.isNotNull(orderBreakingBy)) {
                userSet.add(orderBreakingBy);
            }
            Integer deliveryBy = vo.getDeliveryBy();
            if (ObjUtil.isNotNull(deliveryBy)) {
                userSet.add(deliveryBy);
            }
            Integer installationBy = vo.getInstallationBy();
            if (ObjUtil.isNotNull(installationBy)) {
                userSet.add(installationBy);
            }
            Integer owner = vo.getOwner();
            if (ObjUtil.isNotNull(owner)) {
                userSet.add(owner);
            }
        }
        Map<Integer, String> nameMap = userService.getNameMap(new ArrayList<>(userSet));
        for (ProductionScheduleVO vo : list) {
            vo.setAddressCustomer(vo.getAddress() + " (" + vo.getCustomer() + ")");
            vo.setMeasurementByName(this.dateBy(null, vo.getMeasurementBy(), nameMap));
            vo.setMeasurement(this.getStr(vo.getMeasurementDate(), vo.getMeasurementByName()));
            vo.setDeliveryByName(this.dateBy(null, vo.getDeliveryBy(), nameMap));
            vo.setDelivery(this.getStr(vo.getDeliveryDate(), vo.getDeliveryByName()));
            vo.setOrderBreakingByName(this.dateBy(null, vo.getOrderBreakingBy(), nameMap));
            vo.setOrderBreaking(this.getStr(vo.getOrderBreakingDate(), vo.getOrderBreakingByName()));
            vo.setDrawingByName(this.dateBy(null, vo.getDrawingBy(), nameMap));
            vo.setDrawing(this.getStr(vo.getDrawingDate(), vo.getDrawingByName()));
            vo.setProcess(this.getStrDesc(vo.getProcessDate(), vo.getProcessDescription()));
            vo.setInstallationByName(this.dateBy(null, vo.getInstallationBy(), nameMap));
            vo.setInstallation(this.getStr(vo.getInstallationDate(), vo.getInstallationByName()));
            vo.setOwnerName(this.dateBy(null, vo.getOwner(), nameMap));
            vo.setCreatedByName(this.dateBy(null, vo.getCreatedBy(), nameMap));
            vo.setUpdatedByName(this.dateBy(null, vo.getUpdatedBy(), nameMap));
            vo.setCustomerConfirmation(this.dateBy(vo.getCustomerConfirmationDate(), null, null));
            vo.setProductionOrder(this.dateBy(vo.getProductionOrderDate(), null, null));
            Date customerConfirmationDate = vo.getCustomerConfirmationDate();
            Integer productionCycleRemaining = vo.getProductionCycleRemaining();
            boolean warning = false;
            if (ObjUtil.isNotNull(customerConfirmationDate) && ObjUtil.isNotEmpty(productionCycleRemaining)) {
                DateTime dateTime = DateUtil.offsetDay(customerConfirmationDate, productionCycleRemaining);
                DateTime offset = DateUtil.offsetDay(dateTime, -10);
                int compare = DateUtil.compare(new Date(), offset);
                if (compare > 0) {
                    warning = true;
                }
            }
            vo.setWarning(warning);
        }
    }

    private String getStr(Date measurementDate, String measurementByName) {
        String dateStr = StrUtil.EMPTY;
        if (ObjUtil.isNotNull(measurementDate)) {
            dateStr = TimeZoneUtil.serializeDate(measurementDate, "MM/dd", false);
        }
        String name = StrUtil.EMPTY;
        if (StrUtil.isNotBlank(measurementByName)) {
            name = measurementByName.substring(0, 1);
        }
        if (StrUtil.isBlank(dateStr)) {
            return name;
        }
        if (StrUtil.isBlank(name)) {
            return dateStr;
        }
        return StrUtil.format("{} {}", dateStr, name);
    }

    private String getStrDesc(Date measurementDate, String measurementByName) {
        String dateStr = StrUtil.EMPTY;
        if (ObjUtil.isNotNull(measurementDate)) {
            dateStr = TimeZoneUtil.serializeDate(measurementDate, "MM/dd", false);
        }
        String name = StrUtil.EMPTY;
        if (StrUtil.isNotBlank(measurementByName)) {
            name = measurementByName;
        }
        if (StrUtil.isBlank(dateStr)) {
            return name;
        }
        if (StrUtil.isBlank(name)) {
            return dateStr;
        }
        return StrUtil.format("{} {}", dateStr, name);
    }

    private String dateBy(Date date, Integer userId, Map<Integer, String> nameMap) {
        String dateStr = StrUtil.EMPTY;
        if (ObjUtil.isNotNull(date)) {
            dateStr = TimeZoneUtil.serializeDate(date, "MM/dd", false);
        }
        String name = StrUtil.EMPTY;
        if (ObjUtil.isNotNull(userId)) {
            if (MapUtil.isNotEmpty(nameMap)) {
                name = nameMap.getOrDefault(userId, StrUtil.EMPTY);
            }
        }
        if (StrUtil.isBlank(dateStr)) {
            return name;
        }
        if (StrUtil.isBlank(name)) {
            return dateStr;
        }
        return StrUtil.format("{} {}", dateStr, name);
    }

    private String filterConditionsStr(ProductionScheduleQueryDTO dto) {
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            return "关键字： " + keyword;
        }
        return StrUtil.EMPTY;
    }

    private ProductionScheduleVO convertToVO(ProductionScheduleEntity item){
        ProductionScheduleVO vo = new ProductionScheduleVO();
        vo.setId(item.getId());
        vo.setCustomer(item.getCustomer());
        vo.setContactInfo(item.getContactInfo());
        vo.setAddress(item.getAddress());
        vo.setProductName(item.getProductName());
        vo.setMeasurementDate(item.getMeasurementDate());
        vo.setMeasurementBy(item.getMeasurementBy());
        vo.setDrawingDate(item.getDrawingDate());
        vo.setDrawingBy(item.getDrawingBy());
        vo.setCustomerConfirmationDate(item.getCustomerConfirmationDate());
        vo.setOrderBreakingDate(item.getOrderBreakingDate());
        vo.setOrderBreakingBy(item.getOrderBreakingBy());
        vo.setProductionOrderDate(item.getProductionOrderDate());
        vo.setProcessDate(item.getProcessDate());
        vo.setProcessDescription(item.getProcessDescription());
        vo.setDeliveryDate(item.getDeliveryDate());
        vo.setDeliveryBy(item.getDeliveryBy());
        vo.setInstallationDate(item.getInstallationDate());
        vo.setInstallationBy(item.getInstallationBy());
        vo.setProductionCycleRemaining(item.getProductionCycleRemaining());
        vo.setRemark(item.getRemark());
        vo.setOwner(item.getOwner());
        vo.setCreatedBy(item.getCreatedBy());
        vo.setCreatedTime(item.getCreatedTime());
        vo.setUpdatedTime(item.getUpdatedTime());
        vo.setUpdatedBy(item.getUpdatedBy());
        vo.setInactive(item.getInactive());
        return vo;
    }

    private void setUpdateFieldValue(ProductionScheduleUpdateDTO item, ProductionScheduleEntity entity){
        entity.setCustomer(item.getCustomer());
        entity.setContactInfo(item.getContactInfo());
        entity.setAddress(item.getAddress());
        entity.setProductName(item.getProductName());
        entity.setMeasurementDate(item.getMeasurementDate());
        entity.setMeasurementBy(item.getMeasurementBy());
        entity.setDrawingDate(item.getDrawingDate());
        entity.setDrawingBy(item.getDrawingBy());
        entity.setCustomerConfirmationDate(item.getCustomerConfirmationDate());
        entity.setOrderBreakingDate(item.getOrderBreakingDate());
        entity.setOrderBreakingBy(item.getOrderBreakingBy());
        entity.setProductionOrderDate(item.getProductionOrderDate());
        entity.setProcessDate(item.getProcessDate());
        entity.setProcessDescription(item.getProcessDescription());
        entity.setDeliveryDate(item.getDeliveryDate());
        entity.setDeliveryBy(item.getDeliveryBy());
        entity.setInstallationDate(item.getInstallationDate());
        entity.setInstallationBy(item.getInstallationBy());
        entity.setProductionCycleRemaining(item.getProductionCycleRemaining());
        entity.setRemark(item.getRemark());
        entity.setOwner(item.getOwner());
        entity.setInactive(item.getInactive());
    }

    private ProductionScheduleEntity convertToEntity(ProductionScheduleAddDTO item){
            ProductionScheduleEntity entity = new ProductionScheduleEntity();
        entity.setCustomer(item.getCustomer());
        entity.setContactInfo(item.getContactInfo());
        entity.setAddress(item.getAddress());
        entity.setProductName(item.getProductName());
        entity.setMeasurementDate(item.getMeasurementDate());
        entity.setMeasurementBy(item.getMeasurementBy());
        entity.setDrawingDate(item.getDrawingDate());
        entity.setDrawingBy(item.getDrawingBy());
        entity.setCustomerConfirmationDate(item.getCustomerConfirmationDate());
        entity.setOrderBreakingDate(item.getOrderBreakingDate());
        entity.setOrderBreakingBy(item.getOrderBreakingBy());
        entity.setProductionOrderDate(item.getProductionOrderDate());
        entity.setProcessDate(item.getProcessDate());
        entity.setProcessDescription(item.getProcessDescription());
        entity.setDeliveryDate(item.getDeliveryDate());
        entity.setDeliveryBy(item.getDeliveryBy());
        entity.setInstallationDate(item.getInstallationDate());
        entity.setInstallationBy(item.getInstallationBy());
        entity.setProductionCycleRemaining(item.getProductionCycleRemaining());
        entity.setRemark(item.getRemark());
        entity.setOwner(item.getOwner());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(ProductionScheduleAddDTO dto) {
        ProductionScheduleEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(ProductionScheduleUpdateDTO dto) {
        Integer id = dto.getId();
        ProductionScheduleEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        entity.setId(id);
        baseRepository.updateById(entity);
    }
    @Override
    public ProductionScheduleVO detail(Integer id) {
        ProductionScheduleEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<ProductionScheduleEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.removeBatchByIds(idList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ProductionScheduleEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }
}
