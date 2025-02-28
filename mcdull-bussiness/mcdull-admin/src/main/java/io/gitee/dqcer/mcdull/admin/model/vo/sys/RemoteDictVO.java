package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

/**
 * 码表 VO
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RemoteDictVO implements VO {
    /**
     * 编码
     */
    private String code;

    /**
     * 父级
     */
    private String parentCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 简码
     */
    private String nameShort;

    /**
     * 类别
     */
    private String selectType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 当前是否为默认（1/是 2/否）
     */
    private Integer defaulted;

    /**
     * 状态（1/正常 2/停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * language
     */
    private String language;

    /**
     * 1-未删除，2-删除
     */
    private Integer delFlag;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RemoteDictVO{");
        sb.append("code='").append(code).append('\'');
        sb.append(", parentCode='").append(parentCode).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", nameShort='").append(nameShort).append('\'');
        sb.append(", selectType='").append(selectType).append('\'');
        sb.append(", sort=").append(sort);
        sb.append(", defaulted=").append(defaulted);
        sb.append(", status=").append(status);
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append(", delFlag=").append(delFlag);
        sb.append('}');
        return sb.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getDefaulted() {
        return defaulted;
    }

    public void setDefaulted(Integer defaulted) {
        this.defaulted = defaulted;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
