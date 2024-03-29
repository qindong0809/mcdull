package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * 字典数据 视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class DictDataVO implements VO {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private Long dictCode;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /** 表格字典样式 */
    private String listClass;

    /**
     *  是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 状态
     * @see io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    private Date createdTime;

    public boolean getDefault() {
        return Convert.toBool(this.isDefault);
    }
}
