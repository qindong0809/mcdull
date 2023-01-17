package io.gitee.dqcer.mcdull.mdc.provider.config.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板的读取类 上传数据监听器
 *
 * @author dqcer
 * @date 2022/11/20 22:11:13
 */
public class UploadDataListener extends AnalysisEventListener<TplExport> {
    private List<TplExport> importDataList = new ArrayList<>();


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(TplExport data, AnalysisContext context) {
        importDataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //do nothing
    }

    public List<TplExport> getImportDataList() {
        return importDataList;
    }
}
