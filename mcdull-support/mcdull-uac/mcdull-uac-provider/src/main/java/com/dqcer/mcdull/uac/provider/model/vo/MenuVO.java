package io.gitee.dqcer.uac.provider.model.vo;

import io.gitee.dqcer.framework.base.annotation.Transform;
import io.gitee.dqcer.framework.base.vo.TreeVO;
import io.gitee.dqcer.framework.base.enums.StatusEnum;
import io.gitee.dqcer.uac.provider.model.enums.MenuTypeEnum;

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
     * @see StatusEnum
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuVO{");
        sb.append("status=").append(status);
        sb.append(", statusStr='").append(statusStr).append('\'');
        sb.append(", delFlag=").append(delFlag);
        sb.append(", delFlagStr='").append(delFlagStr).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", resCode='").append(resCode).append('\'');
        sb.append(", type=").append(type);
        sb.append(", typeStr='").append(typeStr).append('\'');
        sb.append(", id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", hasChild=").append(hasChild);
        sb.append(", children=").append(children);
        sb.append('}');
        return sb.toString();
    }

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
