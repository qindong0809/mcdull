package io.gitee.dqcer.mcdull.uac.provider.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 自定义风格策略
 *
 * @author dqcer
 * @since 2024/06/19
 */
public class CustomSheetWriteHandler extends AbstractColumnWidthStyleStrategy implements SheetWriteHandler {

    private Integer totalColumn;

    private Map<Integer,String[]> map;

    private List<Integer> listIndex;

    private Map<Integer, Integer[]> mapLength;

    private Boolean autoFilter;

    private String sheetName;

    public CustomSheetWriteHandler(Map<Integer, String[]> dordownMap, List<Integer> listIndex,
                                   Map<Integer, Integer[]> mapLength, String sheetName,
                                   Boolean autoFilter, Integer totalColumn) {
        this.map = dordownMap;
        this.listIndex = listIndex;
        this.mapLength = mapLength;
        this.sheetName = sheetName;
        this.autoFilter = autoFilter;
        this.totalColumn = totalColumn;
    }

    public CustomSheetWriteHandler(Integer totalColumn) {
        this(Collections.emptyMap(), Collections.emptyList(), Collections.emptyMap(), StrUtil.EMPTY, true, totalColumn);
    }

    @Override
    protected void setColumnWidth(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.setColumnWidth(cell.getColumnIndex(), 5000);
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (this.isDataSheet()) {
            Sheet sheet = writeSheetHolder.getSheet();
            // collNum:表示要冻结的列数；rowNum:表示要冻结的行数；firstCellNum:表示被固定列右边第一列的列号；firstRollNum :表示被固定行下边第一列的行号;
            sheet.createFreezePane(0, 1, 0, 1);
            if (ObjectUtil.isNotEmpty(autoFilter)) {
                // 过滤列
                sheet.setAutoFilter(new CellRangeAddress(0,0,0,totalColumn - 1 ));
            }

            //设置下拉框
            DataValidationHelper helper = sheet.getDataValidationHelper();
            for (Map.Entry<Integer, String[]> entry : map.entrySet()) {
                String hiddenName = StrUtil.format("{}_{}_{}", "hidden", sheetName, entry.getKey());
                //1.创建一个隐藏的sheet 名称为 hidden
                Workbook workbook = writeWorkbookHolder.getWorkbook();
                Sheet hidden = workbook.createSheet(hiddenName);
                Name category1Name = workbook.createName();
                category1Name.setNameName(hiddenName);
                //下拉框的起始行,结束行,起始列,结束列
                CellRangeAddressList addressList = new CellRangeAddressList(1, 2000, entry.getKey(), entry.getKey());
                //获取excel列名
                String excelLine = getExcelLine(entry.getKey());
                //2.循环赋值
                String[] values = entry.getValue();
                for (int i = 0, length = values.length; i < length; i++) {
                    // 3:表示你开始的行数  3表示 你开始的列数
                    Row row = hidden.getRow(i);
                    if (row == null) {
                        row = hidden.createRow(i);
                    }
                    row.createCell(entry.getKey()).setCellValue(values[i]);
                }
                //4.  =hidden!$H:$1:$H$50  sheet为hidden的 H1列开始H50行数据获取下拉数组
                String refers = "=" + hiddenName + "!$" + excelLine +
                        "$1:$" + excelLine + "$" + (values.length);
                //5 将刚才设置的sheet引用到你的下拉列表中
                DataValidationConstraint constraint = helper.createFormulaListConstraint(refers);
                DataValidation dataValidation = helper.createValidation(constraint, addressList);
                if (dataValidation instanceof HSSFDataValidation) {
                    dataValidation.setSuppressDropDownArrow(false);
                } else {
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setShowErrorBox(true);
                }
                // 阻止输入非下拉选项的值
                dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                dataValidation.createErrorBox("错误", "请输入下拉选项的内容");
                writeSheetHolder.getSheet().addValidationData(dataValidation);
                //设置列为隐藏
                int hiddenIndex = workbook.getSheetIndex(hiddenName);
                if (!workbook.isSheetHidden(hiddenIndex)) {
                    workbook.setSheetHidden(hiddenIndex, true);
                }
            }
        }

    }

    public static String getExcelLine(int num) {
        String line = "";
        int first = num / 26;
        int second = num % 26;
        if (first > 0) {
            line = (char) ('A' + first - 1) + "";
        }
        line += (char) ('A' + second) + "";
        return line;
    }

    private boolean isDataSheet() {
        return Convert.toInt(this.totalColumn, 0) != 0;
    }

}
