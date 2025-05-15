package io.gitee.dqcer.mcdull.system.provider.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import io.gitee.dqcer.mcdull.business.common.excel.CustomSheetWriteHandler;
import io.gitee.dqcer.mcdull.business.common.excel.IndexStyleCellWriteHandler;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.provider.model.bo.DynamicFieldBO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel
 *
 * @author dqcer
 * @since 2024/06/18
 */
public class ExcelUtil {


    public static void main(String[] args) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        exportExcelByList(byteArrayOutputStream, "good job",
                StrUtil.EMPTY, StrUtil.EMPTY, StrUtil.EMPTY, ListUtil.of("name", "age"), dataList());
        FileUtil.writeBytes(byteArrayOutputStream.toByteArray(), "D:/22.xlsx");

        ByteArrayOutputStream byteArrayOutputStreamMap = new ByteArrayOutputStream();
        List<Pair<String, String>> list = ListUtil.of(Pair.of("姓名", "name"), Pair.of("年龄", "age"));
        List<Map<String, String>> dataMapList = new ArrayList<>();
        Map<String, String> dataMap =  new TreeMap<>();
        dataMap.put("name", "张三");
        dataMap.put("age", "33");
        dataMapList.add(dataMap);
        exportExcelByMap(byteArrayOutputStreamMap, "map sheet",
                StrUtil.EMPTY, StrUtil.EMPTY, StrUtil.EMPTY, list, dataMapList);

        FileUtil.writeBytes(byteArrayOutputStreamMap.toByteArray(), "D:/33.xlsx");
    }

    private static List<List<String>> dataList() {
        List<List<String>> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            List<String> data = ListUtils.newArrayList();
            data.add("name" + i);
            data.add("0.56");
            list.add(data);
        }
        return list;
    }

    public static void exportExcelByMap(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime, List<Pair<String, String>> list ,
                                   List<Map<String, String>> dataMapList) {
        List<String> titleList = new ArrayList<>();
        for (Pair<String, String> pair : list) {
            String key = pair.getKey();
            titleList.add(key);
        }
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                toTitleList(sheetName, exportBy, exportTime, titleList), toDataList(list, dataMapList));
    }


    public static void exportExcelByList(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime,
                                   List<String> titleList,
                                   List<List<String>> dataList) {
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                dynamicHeader(titleList), dataList);
    }

    private static List<List<String>> dynamicHeader(List<String> titleList) {
        return titleList.stream()
                .map(item -> ListUtil.toList(item.split(",")))
                .collect(Collectors.toList());
    }

    private static void exportExcel(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime,
                                   List<List<String>> titleList,
                                   List<List<String>> dataList) {
        ExcelWriter writer = EasyExcel.write(outputStream).head(titleList)
                .build();
        WriteSheet indexSheet = EasyExcel.writerSheet(GlobalConstant.Excel.INDEX)
                .registerWriteHandler(new IndexStyleCellWriteHandler())
                .needHead(false)
                .build();
        writer.write(indexDataList(filterConditions, exportBy, exportTime), indexSheet);

        WriteSheet dataSheet = EasyExcel.writerSheet(sheetName)
                .registerWriteHandler(defaultStyles())
                // 自动计算列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                .registerWriteHandler(new CustomSheetWriteHandler(titleList.size()))
                .build();
        writer.write(dataList,dataSheet);
        writer.finish();
    }

    public static List<List<Object>> getEmptyDataList(List<DynamicFieldBO> fieldList, int num) {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            List<Object> data = new ArrayList<>();
            int size = fieldList.size();
            for (int j = 0; j < size; j++) {
                data.add(StrUtil.EMPTY);
            }
            list.add(data);
        }
        return list;
    }

    public static HorizontalCellStyleStrategy defaultStyles() {
        // 表头样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("Calibri");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容样式策略策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置背景颜色白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置垂直居中为居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置左右对齐为靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 设置单元格上下左右边框为细边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //创建字体对象
        WriteFont contentWriteFont = new WriteFont();
        //内容字体大小
        contentWriteFont.setFontName("Calibri");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteFont.setBold(false);//不加粗
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 初始化表格样式
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


    public static List<List<Object>> indexDataList(String filterConditions, String exportBy, String exportTime) {
        List<List<Object>> list = ListUtils.newArrayList();
        List<Object> data = ListUtils.newArrayList();
        data.add("过滤条件: ");
        data.add(filterConditions);
        list.add(data);

        data = ListUtils.newArrayList();
        data.add("导出人: ");
        data.add(exportBy);
        list.add(data);

        data = ListUtils.newArrayList();
        data.add("导出时间: ");
        data.add(exportTime);
        list.add(data);
        return list;
    }



    private static List<List<String>> toTitleList(String sheetName, String exportBy, String exportTime, List<String> pairList) {
        if (CollUtil.isEmpty(pairList)) {
            return Collections.emptyList();
        }
        List<List<String>> titleList = new ArrayList<>();
        for (String name : pairList) {
            List<String> oneTitle = new ArrayList<>();
//            oneTitle.add(sheetName);
//            oneTitle.add("导出时间" + exportTime + "导出人" + exportBy);
            oneTitle.add(name);
            titleList.add(oneTitle);
        }

        return titleList;
    }

    private static List<List<String>> toDataList(List<Pair<String, String>> titleList, List<Map<String, String>> dataMapList) {
        if (CollUtil.isEmpty(dataMapList)) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, String> map : dataMapList) {
            List<String> oneData = new ArrayList<>();
            for (Pair<String, String> pair : titleList) {
                oneData.add(map.get(pair.getValue()));
            }
            dataList.add(oneData);
        }
        return dataList;
    }
}
