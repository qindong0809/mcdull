package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 主键
 *
 * @author dqcer
 * @since 2022/01/12
 */
public class IdDO implements DO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    @TableId(type= IdType.AUTO)
    protected Integer id;

    @Override
    public String toString() {
        return "IdDO{" + "id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
