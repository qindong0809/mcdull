package io.gitee.dqcer.mcdull.business.common.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class ExcelUtil {

    public static <T extends AnalysisEventListener<?>> void readExcel(InputStream inputStream, Supplier<T> supplier) {
        ExcelReaderBuilder read = EasyExcel.read(inputStream);
        ExcelReader excelReader = read.build();
        List<ReadSheet> readSheets = excelReader.excelExecutor().sheetList();
        // 跳过index
        List<ReadSheet> readSheetList = new LinkedList<>();
        for (int i = 1; i < readSheets.size(); i++) {
            ReadSheet readSheet1 = readSheets.get(i);
            String sheetName = readSheet1.getSheetName();
            if (StrUtil.containsIgnoreCase(sheetName, "index")) {
                continue;
            }
            T t = supplier.get();
            ReadSheet readSheet = EasyExcel.readSheet(i).registerReadListener(t).headRowNumber(1).build();
            readSheetList.add(readSheet);
        }
        executeWithExceptionHandling(() -> excelReader.read(readSheetList));
    }
    public static <T> void executeWithExceptionHandling(Supplier<T> supplier) {
        try {
            supplier.get();
        } catch (Exception e) {
            log.error("excelImport error", e);
            throw new BusinessException("excel已更新");
        }
    }
}
