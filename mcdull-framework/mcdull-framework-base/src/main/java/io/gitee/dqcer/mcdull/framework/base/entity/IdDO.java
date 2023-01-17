package io.gitee.dqcer.mcdull.framework.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 主键
 *
 * @author dqcer
 * @version 2022/01/12
 */
public class IdDO implements DO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 只有当插入对象ID 为空，才自动填充
     */
    @TableId(type= IdType.ASSIGN_ID)
    protected Long id;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IdDO{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
