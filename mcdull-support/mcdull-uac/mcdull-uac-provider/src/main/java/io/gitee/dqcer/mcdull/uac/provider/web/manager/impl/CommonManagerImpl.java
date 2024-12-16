package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.config.CustomSheetWriteHandler;
import io.gitee.dqcer.mcdull.uac.provider.config.IndexStyleCellWriteHandler;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.util.ExcelUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IConfigService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class CommonManagerImpl implements ICommonManager {

    @Resource
    private IUserManager userManager;

    @Resource
    private IConfigService configService;

    @Resource
    private IFileService fileService;

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
            WriteSheet writeSheet = EasyExcel.writerSheet(0, "Index")
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

    @Override
    public void exportExcel(String sheetName, String conditions, Map<String, String> titleMap,
                            List<Map<String, String>> mapList) {
        Integer userId = UserContextHolder.userId();
        String actualName = this.getUserName(userId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String s = this.convertDateByUserTimezone(new Date());
        ExcelUtil.exportExcelByMap(outputStream, sheetName,
                conditions, actualName, s, titleMap, mapList);
        byte[] byteArray = outputStream.toByteArray();

        String fileName = this.getFileName(FileExtensionTypeEnum.EXCEL_X, sheetName);

        Boolean exportAutoStorage = configService.getConfigToBool("export-auto-storage");
        if (BooleanUtil.isTrue(exportAutoStorage)) {
            byte[] copiedBinaryData = new byte[byteArray.length];
            ArrayUtil.copy(byteArray, copiedBinaryData, byteArray.length);
            String tmpPath = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis() + File.separator + fileName;
            FileUtil.writeBytes(copiedBinaryData, tmpPath);
            fileService.fileUpload(FileUtil.writeBytes(copiedBinaryData, tmpPath), FileFolderTypeEnum.EXPORT.getValue());
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
}
