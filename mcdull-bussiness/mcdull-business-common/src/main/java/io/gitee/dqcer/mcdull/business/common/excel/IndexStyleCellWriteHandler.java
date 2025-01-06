package io.gitee.dqcer.mcdull.business.common.excel;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import org.apache.poi.ss.usermodel.*;

/**
 * index样式单元格写入处理程序
 *
 * @author dqcer
 * @since 2024/12/12
 */
public class IndexStyleCellWriteHandler implements CellWriteHandler {


    @Override
    public void afterCellCreate(CellWriteHandlerContext context) {
        CellWriteHandler.super.afterCellCreate(context);
        Boolean head = context.getHead();
        if (head) {
            return;
        }
        Cell cell = context.getCell();
        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        Font cellFont = workbook.createFont();
        cellFont.setFontHeightInPoints((short) 12);
        cellStyle.setFont(cellFont);
        cell.setCellStyle(cellStyle);

        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        if (rowIndex == 0 && columnIndex == 0
                || rowIndex == 1 && columnIndex == 0
                || rowIndex == 2 && columnIndex == 0 ) {
            cellFont = workbook.createFont();
            cellFont.setBold(true);
            cellStyle.setFont(cellFont);
            cell.setCellStyle(cellStyle);

            Row row = context.getRow();
            row.setHeightInPoints((short) 25);
            context.setRow(row);
        }
        Sheet sheet = context.getWriteSheetHolder().getSheet();
        sheet.setColumnWidth(0, 7500);
        sheet.setColumnWidth(1, 7500);
    }
}
