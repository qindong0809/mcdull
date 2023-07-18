package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 阿俊
 * @create 2023-07-18 10:06
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
public class ExportDetailVO {

    @ExcelProperty("导出时间")
    private LocalDateTime exportTime;

    @ExcelProperty("导出人")
    private String exportUser;

    @ExcelProperty("过滤条件")
    private String filterCondition;

    /**
     * 过滤条件拼接
     * @param condition
     */
    public void concatString(String... condition){
        StringBuilder concat = new StringBuilder();
        for (int i = 0; i < condition.length; i++) {
            concat.append(condition[i]).append(";");
        }
        this.filterCondition = concat.toString();
    }
}
