package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import io.gitee.dqcer.mcdull.business.common.excel.CustomSheetWriteHandler;
import io.gitee.dqcer.mcdull.business.common.excel.DataAnalysisListener;
import io.gitee.dqcer.mcdull.business.common.excel.DynamicFieldTemplate;
import io.gitee.dqcer.mcdull.business.common.excel.IndexStyleCellWriteHandler;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.util.ExcelUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFolderService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
public class CommonManagerImpl implements ICommonManager {

    @Resource
    private IUserManager userManager;
    @Resource
    private IFileService fileService;
    @Resource
    private IConfigRepository configRepository;
    @Resource
    private CacheChannel cacheChannel;
    @Resource
    private IFolderService folderService;
    @Resource
    private IMenuService menuService;

    private String getFileName(FileExtensionTypeEnum fileExtension, String... args) {
        if (ObjUtil.isNull(fileExtension) || ArrayUtil.isEmpty(args)) {
            throw new IllegalArgumentException();
        }
        StrJoiner joiner = new StrJoiner(StrUtil.UNDERLINE);
        joiner.append(args);
        Date now = UserContextHolder.getSession().getNow();
        String dateStr = TimeZoneUtil.serializeDate(now, DatePattern.PURE_DATETIME_PATTERN, false);
        return StrUtil.format("{}_{}{}", joiner, dateStr, fileExtension.getCode());
    }


    @Override
    public String convertDateTimeStr(Date date) {
        if (ObjUtil.isNull(date)) {
            return StrUtil.EMPTY;
        }
        return TimeZoneUtil.serializeDate(date, "yyyyMMdd_HHmmss", false);
    }

    @Override
    public String convertDateByUserTimezone(Date date) {
        if (ObjUtil.isNull(date)) {
            return StrUtil.EMPTY;
        }
        return TimeZoneUtil.serializeDate(date, UserContextHolder.getSession().getDateFormat(), true);
    }

    @SneakyThrows
    @Override
    public void downloadExcelTemplate(Map<String, List<DynamicFieldBO>> sheetTemplateMap, String fileNamePrefix) {
        if (MapUtil.isEmpty(sheetTemplateMap)) {
            return;
        }
        Map<String, List<List<String>>> sheetHeadMap = this.getSheetHeadMap(sheetTemplateMap);
        Map<String, Map<Integer, String[]>> sheetDordownMap = this.getDordownMap(sheetTemplateMap);
        //  时间类型的字段
        List<Integer> listIndex = new ArrayList<>();
        // 长度效验
        Map<Integer, Integer[]> mapLength = new HashMap<>(16);
        String actualName = this.getUserName(UserContextHolder.userId());
        String dateTimeStr = this.convertDateByUserTimezone(new Date());
        HttpServletResponse response = ServletUtil.getResponse();
        String fileName = this.getFileName(FileExtensionTypeEnum.EXCEL_X, fileNamePrefix);
        ServletUtil.setDownloadFileHeader(response, fileName);
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(0, GlobalConstant.Excel.INDEX)
                    .registerWriteHandler(new IndexStyleCellWriteHandler())
                    .build();
            excelWriter.write(ExcelUtil.indexDataList(StrUtil.EMPTY, actualName, dateTimeStr), writeSheet);
            int index = 1;
            for (Map.Entry<String, List<List<String>>> entry : sheetHeadMap.entrySet()) {
                String sheetName = entry.getKey();
                WriteSheet writeSheet2 = EasyExcel.writerSheet(index ++, sheetName)
                        .head(entry.getValue())
                        .registerWriteHandler(ExcelUtil.defaultStyles())
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                        .registerWriteHandler(new CustomSheetWriteHandler(
                                sheetDordownMap.get(sheetName), listIndex, mapLength, sheetName, true, entry.getValue().size()))
                        .build();
                excelWriter.write(ExcelUtil.getEmptyDataList(sheetTemplateMap.get(sheetName), 30), writeSheet2);
            }
        }
    }

    @SneakyThrows
    @Override
    public void importExcelData(MultipartFile file, Function<String, List<DynamicFieldTemplate>> function,
                                Consumer<List<Pair<String, DataAnalysisListener>>> consumer) {
        String filename = file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();
        ExcelReaderBuilder readerBuilder = EasyExcel.read(inputStream);
        ExcelReader excelReader = readerBuilder.build();
        List<ReadSheet> resultList = new LinkedList<>();
        List<Pair<String, DataAnalysisListener>> pairList = new ArrayList<>();
        List<ReadSheet> sheetList = excelReader.excelExecutor().sheetList();
        for (ReadSheet readSheet : sheetList) {
            String sheetName = readSheet.getSheetName();
            if (StrUtil.equalsIgnoreCase(sheetName, GlobalConstant.Excel.INDEX)
                    || StrUtil.containsIgnoreCase(sheetName, GlobalConstant.Excel.HIDDEN)) {
                continue;
            }
            pairList.add(Pair.of(sheetName, new DataAnalysisListener(function.apply(sheetName))));
        }
        excelReader.read(resultList);
        for (Pair<String, DataAnalysisListener> pair : pairList) {
            List<Tuple> errorList = pair.getValue().getErrorList();
            if (CollUtil.isNotEmpty(errorList)) {
                StringBuilder sb = new StringBuilder();
                for (Tuple tuple : errorList) {
                    Object o = tuple.get(0);
                    sb.append(o).append(StrUtil.COMMA);
                }
                String errorMsg = StrUtil.subBefore(sb.toString(), StrUtil.COMMA, false);
                throw new BusinessException(errorMsg);
            }
        }
        consumer.accept(pairList);
    }

    private Map<String, List<List<String>>> getSheetHeadMap(Map<String, List<DynamicFieldBO>> sheetTemplateMap) {
        Map<String, List<List<String>>> sheetHeadMap = new HashMap<>(8);
        for (Map.Entry<String, List<DynamicFieldBO>> entry : sheetTemplateMap.entrySet()) {
            List<List<String>> headList = new ArrayList<>();
            for (DynamicFieldBO dynamicFieldBO : entry.getValue()) {
                List<String> headTitle = new ArrayList<>();
                headTitle.add(dynamicFieldBO.getName());
                headList.add(headTitle);
            }
            sheetHeadMap.put(entry.getKey(), headList);
        }
        return sheetHeadMap;
    }

    private Map<String, Map<Integer, String[]>> getDordownMap(Map<String, List<DynamicFieldBO>> mapField) {
        Map<String, Map<Integer, String[]>> sheetMap = new HashMap<>(8);
        for (Map.Entry<String, List<DynamicFieldBO>> entry : mapField.entrySet()) {
            Map<Integer, String[]> map = new HashMap<>(16);
            List<DynamicFieldBO> excelHeaders = entry.getValue();
            for (int i = 0; i < excelHeaders.size(); i++) {
                DynamicFieldBO formField = excelHeaders.get(i);
                List<String> dropdownList = formField.getDropdownList();
                FormItemControlTypeEnum controlTypeEnum = formField.getControlTypeEnum();
                if (controlTypeEnum.equals(FormItemControlTypeEnum.SELECT)
                        || controlTypeEnum.equals(FormItemControlTypeEnum.RADIO)
                        || controlTypeEnum.equals(FormItemControlTypeEnum.SWITCH)
                        || controlTypeEnum.equals(FormItemControlTypeEnum.CHECKBOX)
                        ) {
                    if (CollectionUtil.isNotEmpty(dropdownList)) {
                        map.put(i, ArrayUtil.toArray(dropdownList, String.class));
                    }
                }
            }
            sheetMap.put(entry.getKey(), map);
        }
        return sheetMap;
    }

    private String getUserName(Integer userId) {
        Map<Integer, String> nameMap = userManager.getNameMap(ListUtil.of(userId));
        return nameMap.get(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <D extends PagedDTO, V extends VO> void exportExcel(D dto, Function<D, PagedVO<V>> function,
                                                                  String sheetName, List<Pair<String, Func1<V, ?>>> titleFuncList) {
        sheetName = StrUtil.isNotBlank(sheetName) ? sheetName : "数据列表";
        PageUtil.setMaxPageSize(dto);
        PagedVO<V> page = function.apply(dto);
        List<V> list = new ArrayList<>();
        if (ObjUtil.isNotNull(page)) {
            list = page.getList();
        }
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (Pair<String, Func1<V, ?>> entry : titleFuncList) {
            pairList.add(Pair.of(entry.getKey(), LambdaUtil.getFieldName(entry.getValue())));
        }
        List<Map<String, String>> mapList = new ArrayList<>();
        for (V vo : list) {
            Map<String, String> hashMap = new HashMap<>();
            for (Pair<String, String> entry : pairList) {
                String fieldName = entry.getValue();
                Object fieldValue = ReflectUtil.getFieldValue(vo, fieldName);
                if (fieldValue instanceof Date) {
                    fieldValue = this.convertDateByUserTimezone((Date) fieldValue);
                }
                if (fieldValue instanceof Boolean) {
                    fieldValue = BooleanUtil.toString(Convert.toBool(fieldValue), "是", "否");
                }
                Pair<String, String> pair = pairList.stream().filter(i -> i.getKey().equals(entry.getKey())).findFirst().orElse(null);
                if (pair != null) {
                    hashMap.put(pair.getValue(), Convert.toStr(fieldValue));
                }
            }
            mapList.add(hashMap);
        }
        this.exportExcel(sheetName, StrUtil.EMPTY, pairList, mapList);
    }

    @Override
    public void exportExcel(String sheetName, String conditions, List<Pair<String, String>> pairList,
                            List<Map<String, String>> mapList) {
        Integer userId = UserContextHolder.userId();
        String actualName = this.getUserName(userId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String s = this.convertDateByUserTimezone(new Date());
        ExcelUtil.exportExcelByMap(outputStream, sheetName,
                conditions, actualName, s, pairList, mapList);
        byte[] byteArray = outputStream.toByteArray();
        List<String> menuName = menuService.getCurrentMenuName();
        String fileName = this.getFileName(FileExtensionTypeEnum.EXCEL_X, StrUtil.join(StrUtil.UNDERLINE, menuName));
        Boolean exportAutoStorage = this.getConfigToBool("export-auto-storage");
        if (BooleanUtil.isTrue(exportAutoStorage)) {
            byte[] copiedBinaryData = new byte[byteArray.length];
            ArrayUtil.copy(byteArray, copiedBinaryData, byteArray.length);
            String tmpPath = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis() + File.separator + fileName;
            FileUtil.writeBytes(copiedBinaryData, tmpPath);
            fileService.fileUpload(FileUtil.writeBytes(copiedBinaryData, tmpPath), folderService.getSystemFolderId());
            FileUtil.del(tmpPath);
        }
        HttpServletResponse response = ServletUtil.getResponse();
        ServletUtil.setDownloadFileHeader(response, fileName);
        try {
            response.getOutputStream().write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String readTemplateFileContent(String path) {
        try (InputStream inputStream = ResourceUtil.getStream(path)) {
            if (inputStream != null) {
                byte[] bytes = IoUtil.readBytes(inputStream);
                return new String(bytes, StandardCharsets.UTF_8);
            }
            return StrUtil.EMPTY;
        } catch (Exception e) {
            LogHelp.error(log, "模版文件: {}", path, e);
            throw new BusinessException("找不到对应的模版文件");
        }
    }

    @Override
    public String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }

    @Override
    public String getConfig(String key) {
        List<?> list = cacheChannel.get("sys_config", List.class);
        String value = null;
        if (CollUtil.isNotEmpty(list)) {
            for (Object o : list) {
                ConfigEntity entity = (ConfigEntity) o;
                if (entity.getConfigKey().equals(key)) {
                    value = entity.getConfigValue();
                    break;
                }
            }
        }
        List<ConfigEntity> entityList = configRepository.list();
        if (CollUtil.isNotEmpty(entityList)) {
            cacheChannel.put("sys_config", entityList, 60 * 60 * 24);
            ConfigEntity configEntity = entityList.stream()
                    .filter(entity -> entity.getConfigKey().equals(key)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(configEntity)) {
                return configEntity.getConfigValue();
            }
        }
        return value;
    }

    @Override
    public Boolean isCaptchaEnabled() {
        String config = this.getConfig("login-captcha");
        if (StrUtil.isNotBlank(config)) {
            return BooleanUtil.toBooleanObject(config);
        }
        return false;
    }

    @Override
    public Boolean getConfigToBool(String key) {
        String config = this.getConfig(key);
        if (StrUtil.isNotBlank(config)) {
            return BooleanUtil.toBooleanObject(config);
        }
        return null;
    }

}
