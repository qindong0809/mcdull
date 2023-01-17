package io.gitee.dqcer.mcdull.mdc.provider.config.excel;


import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author dqcer
 * @description 
 * @date 14:08 2021/2/24
 */
public class TplValidVo {

    @ExcelProperty({"受试者导入模板", "试验中心"})
    private String siteName;

    @ExcelProperty({"受试者导入模板", "试验中心编号"})
    private Long siteId;

    @ExcelProperty({"受试者导入模板", "筛选号"})
    private String subjectNo;

    @ExcelProperty({"受试者导入模板", "知情同意书版本"})
    private String icVersion;

    @ExcelProperty({"受试者导入模板", "知情同意时间"})
    private Date icTime;

    @ExcelProperty({"受试者导入模板", "受试者状态"})
    private String activeStatusCode;

    @ExcelProperty({"受试者导入模板", "受试者状态"})
    private String activeStatusCodeStr;


    @ExcelProperty({"受试者导入模板", "筛选时间"})
    private Date screenTime;

    @ExcelProperty({"受试者导入模板", "筛选失败时间"})
    private Date screenErrorTime;

    @ExcelProperty({"受试者导入模板", "筛选失败原因"})
    private String screenErrorReasonCode;

    @ExcelProperty({"受试者导入模板", "筛选失败原因描述"})
    private String screenErrorReasonDec;

    @ExcelProperty({"受试者导入模板", "入组时间"})
    private Date enrollmentTime;

    @ExcelProperty({"受试者导入模板", "脱落时间"})
    private Date withdrawalTime;

    @ExcelProperty({"受试者导入模板", "脱落原因"})
    private String withdrawalReasonCode;

    @ExcelProperty({"受试者导入模板", "脱落原因描述"})
    private String withdrawalReasonDec;

    @ExcelProperty({"受试者导入模板", "完成时间"})
    private Date finishTime;

    /**
     * 备注
     */
    private List<String> remark;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo;
    }

    public String getIcVersion() {
        return icVersion;
    }

    public void setIcVersion(String icVersion) {
        this.icVersion = icVersion;
    }

    public Date getIcTime() {
        return icTime;
    }

    public void setIcTime(Date icTime) {
        this.icTime = icTime;
    }

    public String getActiveStatusCode() {
        return activeStatusCode;
    }

    public void setActiveStatusCode(String activeStatusCode) {
        this.activeStatusCode = activeStatusCode;
    }

    public String getActiveStatusCodeStr() {
        return activeStatusCodeStr;
    }

    public void setActiveStatusCodeStr(String activeStatusCodeStr) {
        this.activeStatusCodeStr = activeStatusCodeStr;
    }

    public Date getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(Date screenTime) {
        this.screenTime = screenTime;
    }

    public Date getScreenErrorTime() {
        return screenErrorTime;
    }

    public void setScreenErrorTime(Date screenErrorTime) {
        this.screenErrorTime = screenErrorTime;
    }

    public String getScreenErrorReasonCode() {
        return screenErrorReasonCode;
    }

    public void setScreenErrorReasonCode(String screenErrorReasonCode) {
        this.screenErrorReasonCode = screenErrorReasonCode;
    }

    public String getScreenErrorReasonDec() {
        return screenErrorReasonDec;
    }

    public void setScreenErrorReasonDec(String screenErrorReasonDec) {
        this.screenErrorReasonDec = screenErrorReasonDec;
    }

    public Date getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setEnrollmentTime(Date enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    public Date getWithdrawalTime() {
        return withdrawalTime;
    }

    public void setWithdrawalTime(Date withdrawalTime) {
        this.withdrawalTime = withdrawalTime;
    }

    public String getWithdrawalReasonCode() {
        return withdrawalReasonCode;
    }

    public void setWithdrawalReasonCode(String withdrawalReasonCode) {
        this.withdrawalReasonCode = withdrawalReasonCode;
    }

    public String getWithdrawalReasonDec() {
        return withdrawalReasonDec;
    }

    public void setWithdrawalReasonDec(String withdrawalReasonDec) {
        this.withdrawalReasonDec = withdrawalReasonDec;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public List<String> getRemark() {
        return remark;
    }

    public void setRemark(List<String> remark) {
        this.remark = remark;
    }
}
