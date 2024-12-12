package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.util.ExcelUtil;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommonManagerImpl implements ICommonManager {

    @Resource
    private IUserManager userManager;

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
        HttpServletResponse response = ServletUtil.getResponse();
        ServletUtil.setDownloadFileHeader(response, fileName, (long) byteArray.length);
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
