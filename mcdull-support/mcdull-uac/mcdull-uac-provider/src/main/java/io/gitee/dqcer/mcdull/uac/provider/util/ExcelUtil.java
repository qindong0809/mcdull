package io.gitee.dqcer.mcdull.uac.provider.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.FormulaData;
import com.alibaba.excel.metadata.data.RichTextStringData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.google.common.collect.Lists;
import io.gitee.dqcer.mcdull.uac.provider.config.CustomSheetWriteHandler;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel有用
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
        MapBuilder<String, String> mapBuilder =  MapBuilder.create();
        MapBuilder<String, String> put = mapBuilder.put("姓名", "name").put("年龄", "age");
        Map<String, String> map = new TreeMap<>();
        map.put("姓名", "name");
        map.put("年龄", "age");
        List<Map<String, String>> dataMapList = new ArrayList<>();
        Map<String, String> dataMap =  new TreeMap<>();
        dataMap.put("name", "张三");
        dataMap.put("age", "33");
        dataMapList.add(dataMap);
        exportExcelByMap(byteArrayOutputStreamMap, "map sheet",
                StrUtil.EMPTY, StrUtil.EMPTY, StrUtil.EMPTY, map, dataMapList);

        FileUtil.writeBytes(byteArrayOutputStream.toByteArray(), "D:/33.xlsx");

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
                                   String exportTime,
                                   Map<String, String> titleMap,
                                   List<Map<String, String>> dataMapList) {
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                toTitleList(titleMap), toDataList(titleMap, dataMapList));
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
                .map(item -> Lists.newArrayList(item.split(",")))
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
        WriteSheet indexSheet = EasyExcel.writerSheet("Index")
                .needHead(false)
                .build();
        writer.write(indexDataList(filterConditions, exportBy, exportTime), indexSheet);

        WriteSheet dataSheet = EasyExcel.writerSheet(sheetName)
                .registerWriteHandler(new CustomSheetWriteHandler(titleList.size()))
                .build();
        writer.write(dataList,dataSheet);
        writer.finish();
    }

    private static List<List<Object>> indexDataList(String filterConditions, String exportBy, String exportTime) {
        List<List<Object>> list = ListUtils.newArrayList();
        List<Object> data = ListUtils.newArrayList();

        WriteCellData<Object> objectWriteCellData = new WriteCellData<>("过滤条件");
        WriteCellStyle style = new WriteCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        objectWriteCellData.setWriteCellStyle(style);
        objectWriteCellData.setFormulaData(new FormulaData());
        objectWriteCellData.setRichTextStringDataValue(new RichTextStringData());
        data.add(objectWriteCellData);
        data.add(new WriteCellData<>(filterConditions));
        list.add(data);

        List<Object> data2 = ListUtils.newArrayList();
        data2.add(new WriteCellData<>("导出人"));
        data2.add(new WriteCellData<>(exportBy));
        list.add(data2);

        List<Object> data3 = ListUtils.newArrayList();
        data3.add(new WriteCellData<>("导出时间"));
        data3.add(new WriteCellData<>(exportTime));
        list.add(data3);
        return list;
    }

    private static List<List<String>> toTitleList(Map<String, String> titleMap) {
        if (MapUtil.isEmpty(titleMap)) {
            return Collections.emptyList();
        }
        List<List<String>> titleList = new ArrayList<>();
        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
            List<String> oneTitle = new ArrayList<>();
            oneTitle.add(entry.getKey());
            titleList.add(oneTitle);
        }
        return titleList;
    }

    private static List<List<String>> toDataList(Map<String, String> titleMap, List<Map<String, String>> dataMapList) {
        if (CollUtil.isEmpty(dataMapList)) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, String> map : dataMapList) {
            List<String> oneData = new ArrayList<>();
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                oneData.add(map.get(entry.getValue()));
            }
            dataList.add(oneData);
        }
        return dataList;
    }
}
