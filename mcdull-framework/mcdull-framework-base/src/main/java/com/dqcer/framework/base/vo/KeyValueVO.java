package com.dqcer.framework.base.vo;

/**
 * key value 视图对象
 *
 * @author dqcer
 * @date 2022/12/07
 */
public class KeyValueVO<K, V> implements VO{

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    protected K id;

    /**
     * value
     */
    protected V name;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("KeyValueVO{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append('}');
        return sb.toString();
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public V getName() {
        return name;
    }

    public void setName(V name) {
        this.name = name;
    }
}
