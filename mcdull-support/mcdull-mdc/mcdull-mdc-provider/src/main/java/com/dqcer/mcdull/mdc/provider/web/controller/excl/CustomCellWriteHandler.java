package com.dqcer.mcdull.mdc.provider.web.controller.excl;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
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
public class CustomCellWriteHandler implements CellWriteHandler {

    Map<String, String> codeNameMap;

    public CustomCellWriteHandler(Map<String, String> codeNameMap) {
        this.codeNameMap = codeNameMap;
    }


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 这里可以对cell进行任何操作

    }

}