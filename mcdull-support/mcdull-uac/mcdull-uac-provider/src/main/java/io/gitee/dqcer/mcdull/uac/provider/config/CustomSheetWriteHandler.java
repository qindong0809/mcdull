package io.gitee.dqcer.mcdull.uac.provider.config;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 自定义风格策略
 *
 * @author dqcer
 * @since 2024/06/19
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {

    public int colSplit = 0, rowSplit = 1, leftmostColumn = 0, topRow = 1;
    private Integer totalColumn;

    public CustomSheetWriteHandler(Integer totalCol) {
        this.totalColumn = totalCol;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        //冻结第一行,冻结行下侧第一行的左边框显示“2”
        sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
        // 过滤列
        sheet.setAutoFilter(new CellRangeAddress(0,0,0,totalColumn - 1 ));

    }
}
