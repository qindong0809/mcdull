package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Area 实体类
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_area")
public class AreaEntity  extends IdEntity<Integer> {

    /**
     * 区域编码
     */
    private String code;
    /**
     * 区域上级标识
     */
    private String ids;
    /**
     * 区域等级
     */
    private Integer levelinfo;
    /**
     * 地名简称
     */
    private String name;
    /**
     * 类型
     */
    private Integer areaType;
    /**
     * 全称
     */
    private String fullname;
    /**
     * 邮政编码
     */
    private String govcode;
    /**
     * 维度
     */
    private BigDecimal lat;
    /**
     * 经度
     */
    private BigDecimal lng;
    /**
     * pid
     */
    private Integer pid;
}