package com.dqcer.mcdull.mdc.provider.web.controller.excl;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.AbstractCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;


/**
 * 自定义单元编写处理程序
 *
 * @author dongqin
 * @date 2022/11/20 22:11:22
 */
public class CustomCellWriteHandler extends AbstractCellWriteHandler {

    Map<String, String> codeNameMap;

    public CustomCellWriteHandler(Map<String, String> codeNameMap) {
        this.codeNameMap = codeNameMap;
    }


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对cell进行任何操作
        if (isHead && cell.getColumnIndex() == 0 &&  cell.getRowIndex() == 0) {
            setCellValue(cell, "300130");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 0 ) {
            setCellValue(cell, "300131");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 1 ) {
            setCellValue(cell, "300132");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 2 ) {
            setCellValue(cell, "300133");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 3 ) {
            setCellValue(cell, "300134");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 4 ) {
            setCellValue(cell, "300135");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 5 ) {
            setCellValue(cell, "300136");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 6 ) {
            setCellValue(cell, "300137");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 7 ) {
            setCellValue(cell, "300138");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 8 ) {
            setCellValue(cell, "300139");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 9 ) {
            setCellValue(cell, "300140");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 10 ) {
            setCellValue(cell, "300141");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 11 ) {
            setCellValue(cell, "300142");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 12 ) {
            setCellValue(cell, "300143");
        }
        if (isHead &&  cell.getRowIndex() == 1 && cell.getColumnIndex() == 13 ) {
            setCellValue(cell, "300144");
        }
    }

    private void setCellValue(Cell cell, String code) {
        String s = codeNameMap.get(code);
        if (s != null) {
            cell.setCellValue(s);
        }
    }

}