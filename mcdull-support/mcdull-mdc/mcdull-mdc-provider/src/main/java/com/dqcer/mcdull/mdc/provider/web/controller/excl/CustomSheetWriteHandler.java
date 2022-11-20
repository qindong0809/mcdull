package com.dqcer.mcdull.mdc.provider.web.controller.excl;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义表编写处理程序
 *
 * @author dqcer
 * @date 2022/11/20 22:11:46
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {

    private Map<Integer,String[]> map = null;

    private List<Integer> listIndex = null;

    private Map<Integer, Integer[]> mapLength = null;

    public CustomSheetWriteHandler(Map<Integer,String[]> map, List<Integer> listIndex, Map<Integer, Integer[]> mapLength) {
        this.map = map;
        this.listIndex = listIndex;
        this.mapLength = mapLength;
    }
 
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }
 
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

        // 这里可以对cell进行任何操作
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // k 为存在下拉数据集的单元格下表 v为下拉数据集
        map.forEach((k, v) -> {
            // 下拉列表约束数据
            DataValidationConstraint constraint = helper.createExplicitListConstraint(v);
            // 设置下拉单元格的首行 末行 首列 末列
            CellRangeAddressList rangeList = new CellRangeAddressList(2, 65536, k, k);
            // 设置约束
            DataValidation validation = helper.createValidation(constraint, rangeList);
            // 阻止输入非下拉选项的值
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.setShowErrorBox(true);
            validation.setSuppressDropDownArrow(true);
            validation.createErrorBox("操作失败","请选择对应下拉框中的值");

            sheet.addValidationData(validation);
        });

        for (Integer index : listIndex) {
            CellRangeAddressList rangeListDate = new CellRangeAddressList(2, 65536, index, index);
            DataValidationHelper helperDate = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraintDate = helperDate.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN,"Date(1900, 1, 1)","Date(2099, 12, 31)","yyyy-MM-dd");
            DataValidation dataValidation = helperDate.createValidation(constraintDate, rangeListDate);
            // 输入无效值时是否显示错误框
            dataValidation.setShowErrorBox(true);
            // 验证输入数据是否真确
            dataValidation.setSuppressDropDownArrow(true);
            // 设置无效值时 是否弹出提示框
            dataValidation.setShowPromptBox(true);
            // 设置提示框内容 createPromptBox
            // 设置无效值时的提示框内容 createErrorBox
            dataValidation.createErrorBox("操作失败","请输入[yyyy-MM-dd]格式日期，范围在[1900-01-01 ~ 2099-12-31]之间");
            writeSheetHolder.getSheet().addValidationData(dataValidation);
        }

        Set<Map.Entry<Integer, Integer[]>> entries = mapLength.entrySet();
        for (Map.Entry<Integer, Integer[]> map : entries) {
            Integer k = map.getKey();
            Integer[] v = map.getValue();

            String min = v[0].toString();
            String max = v[1].toString();

            CellRangeAddressList rangeList = new CellRangeAddressList(2, 65536, k, k);
            DataValidationHelper helperDate = writeSheetHolder.getSheet().getDataValidationHelper();
            DataValidationConstraint constraint = helperDate.createTextLengthConstraint(DataValidationConstraint.OperatorType.BETWEEN,  min, max);
            DataValidation dataValidation = helperDate.createValidation(constraint, rangeList);
            dataValidation.setShowErrorBox(true);
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowPromptBox(true);
            dataValidation.createErrorBox("操作失败",String.format("该单元格值的长度应在【%s ~ %s】字符之间", min, max));
            writeSheetHolder.getSheet().addValidationData(dataValidation);
        }
    }
}