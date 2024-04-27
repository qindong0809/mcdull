package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.gitee.dqcer.mcdull.framework.base.support.Entity;

/**
 * 主键
 *
 * @author dqcer
 * @since 2022/01/12
 */
public class IdEntity<T> implements Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    @TableId(type= IdType.AUTO)
    protected T id;

    @Override
    public String toString() {
        return "IdDO{" + "id=" + id +
                '}';
    }

    public T getId() {
        return id;
    }

    public IdEntity<T> setId(T id) {
        this.id = id;
        return this;
    }
}
