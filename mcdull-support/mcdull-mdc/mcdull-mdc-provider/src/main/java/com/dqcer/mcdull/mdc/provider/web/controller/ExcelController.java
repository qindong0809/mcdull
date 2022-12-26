package com.dqcer.mcdull.mdc.provider.web.controller;

import com.alibaba.excel.EasyExcel;
import com.dqcer.mcdull.mdc.provider.config.excel.DropDownSetField;
import com.dqcer.mcdull.mdc.provider.config.excel.DropDownSetInterface;
import com.dqcer.mcdull.mdc.provider.config.excel.ExcelLength;
import com.dqcer.mcdull.mdc.provider.config.excel.TplExport;
import com.dqcer.mcdull.mdc.provider.web.controller.excl.CustomSheetWriteHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel控制器
 *
 * @author dqcer
 * @version 2022/12/26 21:12:21
 */
@RestController
public class ExcelController {

    @Resource
    private DropDownSetInterface dropDownSetInterface;


    /**
     * 下载模板
     *
     * @param studyId  项目ID
     * @param response response
     * @throws IOException IO Exception
     */
    @GetMapping("/download")
    public void download(Long studyId, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode( "import_model", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        Field[] fields = TplExport.class.getDeclaredFields();
        // 响应字段对应的下拉集合
        Map<Integer, String[]> map = new HashMap<>(16);
        //  时间类型的字段
        List<Integer> listIndex = new ArrayList<>();
        // 长度效验
        Map<Integer, Integer[]> mapLength = new HashMap<>(16);

        Field field = null;
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            DropDownSetField dropDownSetField = field.getAnnotation(DropDownSetField.class);

            if (null != dropDownSetField) {
                String[] sources = dropDownSetInterface.getSource(dropDownSetField.value(), null, null, studyId);
                if (null != sources && sources.length > 0) {
                    map.put(i, sources);
                }
            }
            if (field.getType().equals(Date.class)) {
                listIndex.add(i);
            }
            ExcelLength excelLength = field.getAnnotation(ExcelLength.class);
            if (null != excelLength) {
                mapLength.put(i, new Integer[]{excelLength.min(), excelLength.max()});
            }
        }

        EasyExcel.write(response.getOutputStream(), TplExport.class).autoCloseStream(Boolean.FALSE).registerWriteHandler(new CustomSheetWriteHandler(map, listIndex, mapLength))
               .sheet("hello").doWrite((Collection<?>) null);

    }
}
