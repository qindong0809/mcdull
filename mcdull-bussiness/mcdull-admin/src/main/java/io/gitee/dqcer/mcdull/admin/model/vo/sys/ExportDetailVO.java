package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 阿俊
 * @create 2023-07-18 10:06
 */
@Data
@ExcelIgnoreUnannotated
public class ExportDetailVO {

    @ExcelProperty("导出时间")
    private Date exportDate;

    @ExcelProperty("导出人")
    private String exportUser;

    @ExcelProperty("过滤条件")
    private String filterCondition;



}
