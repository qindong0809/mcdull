package com.dqcer.mcdull.uac.provider.model.vo;

import com.dqcer.framework.base.annotation.Transform;
import com.dqcer.framework.base.vo.TreeVO;
import com.dqcer.mcdull.uac.provider.model.enums.MenuTypeEnum;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @version  2022/11/27
 */
public class MenuVO extends TreeVO<MenuVO, Long> {

    /**
     * 状态
     *
     * @see com.dqcer.framework.base.enums.StatusEnum
     */
    private Integer status;

    @Transform(from = "status", param = "status_type")
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type")
    private String delFlagStr;

    /**
     * 名称
     */
    private String name;


    /**
     * 资源编码
     */
    private String resCode;


    /**
     * 类型
     */
    private Integer type;

    @Transform(from = "type", dataSource = MenuTypeEnum.class)
    private String typeStr;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagStr() {
        return delFlagStr;
    }

    public void setDelFlagStr(String delFlagStr) {
        this.delFlagStr = delFlagStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
