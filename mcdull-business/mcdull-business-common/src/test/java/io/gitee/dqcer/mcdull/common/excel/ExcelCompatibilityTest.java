package io.gitee.dqcer.mcdull.common.excel;

import cn.hutool.core.lang.Pair;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelCompatibilityTest {

    @Test
    void easyExcel4WithPoi5WriteAndRead() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Pair<String, String>> titles = List.of(
                Pair.of("姓名", "name"),
                Pair.of("年龄", "age"));
        List<Map<String, String>> data = List.of(Map.of("name", "张三", "age", "30"));

        ExcelWriterUtil.exportExcelByMap(out, "用户", "无", "tester", "2026-11-24", titles, data);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(out.toByteArray()))) {
            assertTrue(workbook.getNumberOfSheets() >= 2, "应包含索引 Sheet 与数据 Sheet");
            assertEquals("用户", workbook.getSheetName(1));
            assertEquals("姓名", workbook.getSheetAt(1).getRow(0).getCell(0).getStringCellValue());
            assertEquals("张三", workbook.getSheetAt(1).getRow(1).getCell(0).getStringCellValue());
        }
    }
}
