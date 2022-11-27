package com.dqcer.framework.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 主键
 *
 * @author dqcer
 * @version 2022/01/12
 */
@SuppressWarnings("unused")
public abstract class SuperId implements Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    @TableId(type= IdType.ASSIGN_ID)
    protected Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
