package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.util.ExcelUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Service
public class CommonManagerImpl implements ICommonManager {

    @Resource
    private IUserManager userManager;

    @Resource
    private IFileService fileService;

    @Override
    public String getFileName(FileExtensionTypeEnum fileExtension, String... args) {
        if (ObjUtil.isNull(fileExtension) || ArrayUtil.isEmpty(args)) {
            throw new IllegalArgumentException();
        }
        StrJoiner joiner = new StrJoiner(StrUtil.UNDERLINE);
        joiner.append(args);
        Date now = UserContextHolder.getSession().getNow();
        String dateStr = TimeZoneUtil.serializeDate(now, DatePattern.PURE_DATETIME_PATTERN);
        return StrUtil.format("{}_{}{}", joiner, dateStr, fileExtension.getCode());
    }

    @Override
    public void storageExcel(String modelName, String suffixFileName, String condition,
                        Supplier<Map<String, String>> supplierTitleMap,
                        Supplier<List<Map<String, String>>> supplierDataList) {
        Integer userId = UserContextHolder.userId();
        String actualName = this.getUserName(userId);
        String dateTimeStr = TimeZoneUtil.serializeDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        String fileName = this.getFileName(FileExtensionTypeEnum.EXCEL_X, suffixFileName);
        LogHelp.info(log, "Storage. actualName: {} fileName: {}", modelName, actualName, fileName);
        ByteArrayOutputStream byteArrayOutputStreamMap = new ByteArrayOutputStream();
        ExcelUtil.exportExcelByMap(byteArrayOutputStreamMap, suffixFileName, condition, actualName, dateTimeStr,
                supplierTitleMap.get(), supplierDataList.get());

        // TODO: 2024/9/28 存储到文件
        // lock
        //byteArrayOutputStreamMap
        MultipartFile multipartFile = null;

        fileService.fileUpload(multipartFile, FileFolderTypeEnum.EXPORT.getValue());
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
        String s = TimeZoneUtil.serializeDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        ExcelUtil.exportExcelByMap(outputStream, sheetName,
                conditions, actualName, s, titleMap, mapList);
        byte[] byteArray = outputStream.toByteArray();
        String fileName = this.getFileName(FileExtensionTypeEnum.EXCEL_X, sheetName);
        HttpServletResponse response = ServletUtil.getResponse();
        ServletUtil.setDownloadFileHeader(response, fileName, (long) byteArray.length);
        try {
            response.getOutputStream().write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
