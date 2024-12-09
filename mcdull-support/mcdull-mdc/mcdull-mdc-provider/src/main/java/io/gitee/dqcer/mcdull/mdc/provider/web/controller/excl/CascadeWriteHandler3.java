package io.gitee.dqcer.mcdull.mdc.provider.web.controller.excl;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;

public class CascadeWriteHandler3 implements SheetWriteHandler {

    private List<String> provinceList; // 省份列表
    private Map<String, List<String>> cityMap; // 省份与城市的映射
    private Map<String, List<String>> districtMap; // 城市与区的映射

    private String[] contactTypes = new String[]{"手机", "座机", "呼机"}; //

    public CascadeWriteHandler3(List<String> provinceList, Map<String, List<String>> cityMap, Map<String, List<String>> districtMap) {
        this.provinceList = provinceList;
        this.cityMap = cityMap;
        this.districtMap = districtMap;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 此处代码在创建Sheet前，通常为空
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {


        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = writeSheetHolder.getSheet();

        // 创建标题行并合并单元格
        Row titleRow = sheet.createRow(0); // 创建第一行作为标题行
        titleRow.setHeightInPoints(2 * sheet.getDefaultRowHeightInPoints()); // 设置行高为默认的两倍
        Cell titleCell = titleRow.createCell(0); // 在第一行第一个单元格中设置标题
        titleCell.setCellValue("人员信息\n完整的"); // 设置标题内容，并添加换行

        // 设置单元格样式为左对齐并自动换行
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.LEFT); // 设置水平对齐为左对齐
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直对齐为居中
        titleStyle.setWrapText(true); // 设置自动换行
        titleCell.setCellStyle(titleStyle); // 应用样式到单元格


        // 合并标题行的单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        // 设置列标题
        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("省");
        headerRow.createCell(1).setCellValue("市");
        headerRow.createCell(2).setCellValue("区");
        headerRow.createCell(3).setCellValue("联系方式");

        // 设置列宽以便显示标题
        sheet.setColumnWidth(0, 256 * 16); // 设置"省"的列宽
        sheet.setColumnWidth(1, 256 * 16); // 设置"市"的列宽
        sheet.setColumnWidth(2, 256 * 16); // 设置"区"的列宽
        sheet.setColumnWidth(3, 256 * 16); // 设置"联系方式"的列宽

        // 创建隐藏的Sheet存放省市区数据
        Sheet hideSheet = workbook.createSheet("siteInfo");
        workbook.setSheetHidden(workbook.getSheetIndex(hideSheet), true);

        // 省份列表处理逻辑
        int rowNum = 0;
        Row provinceRow = hideSheet.createRow(rowNum++);
        provinceRow.createCell(0).setCellValue("省份列表");
        for (int i = 0; i < provinceList.size(); i++) {
            provinceRow.createCell(i + 1).setCellValue(provinceList.get(i));
        }

        // 城市列表处理逻辑
        for (String province : provinceList) {
            List<String> cities = cityMap.get(province);
            Row cityRow = hideSheet.createRow(rowNum++);
            cityRow.createCell(0).setCellValue(province);
            for (int i = 0; i < cities.size(); i++) {
                cityRow.createCell(i + 1).setCellValue(cities.get(i));
            }
            Name cityRangeName = workbook.createName();
            cityRangeName.setNameName(province);
            String cityRange = getRange(1, rowNum, cities.size());
            cityRangeName.setRefersToFormula("siteInfo!" + cityRange);
        }

        // 区列表处理逻辑
        for (String city : cityMap.values().stream().flatMap(List::stream).toArray(String[]::new)) {
            List<String> districts = districtMap.get(city);
            Row districtRow = hideSheet.createRow(rowNum++);
            districtRow.createCell(0).setCellValue(city);
            for (int i = 0; i < districts.size(); i++) {
                districtRow.createCell(i + 1).setCellValue(districts.get(i));
            }
            Name districtRangeName = workbook.createName();
            districtRangeName.setNameName(city);
            String districtRange = getRange(1, rowNum, districts.size());
            districtRangeName.setRefersToFormula("siteInfo!" + districtRange);
        }

        // 省份数据验证
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint provinceConstraint = dvHelper.createExplicitListConstraint(provinceList.toArray(new String[0]));
        CellRangeAddressList provinceRangeAddressList = new CellRangeAddressList(2, 1000, 0, 0);
        setValidation(sheet, dvHelper, provinceConstraint, provinceRangeAddressList);

        // 城市数据验证
        for (int i = 3; i <= 1000; i++) {
            DataValidationConstraint cityConstraint = dvHelper.createFormulaListConstraint("INDIRECT($A$" + i + ")");
            CellRangeAddressList cityRangeAddressList = new CellRangeAddressList(i - 1, i - 1, 1, 1);
            setValidation(sheet, dvHelper, cityConstraint, cityRangeAddressList);
        }

        // 区数据验证
        for (int i = 3; i <= 1000; i++) {
            DataValidationConstraint districtConstraint = dvHelper.createFormulaListConstraint("INDIRECT($B$" + i + ")");
            CellRangeAddressList districtRangeAddressList = new CellRangeAddressList(i - 1, i - 1, 2, 2);
            setValidation(sheet, dvHelper, districtConstraint, districtRangeAddressList);
        }

        // 联系方式数据验证
        DataValidationConstraint contactTypeConstraint = dvHelper.createExplicitListConstraint(new String[]{"手机", "座机", "呼机"});
        CellRangeAddressList contactTypeRangeAddressList = new CellRangeAddressList(2, 1000, 3, 3);
        setValidation(sheet, dvHelper, contactTypeConstraint, contactTypeRangeAddressList);
    }

// 其他辅助方法和类成员不变

    private void setValidation(Sheet sheet, DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList addressList) {
        DataValidation validation = helper.createValidation(constraint, addressList);
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        sheet.addValidationData(validation);
    }

    public String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }
}
