package com.dqcer.mcdull.framework.base.vo;

/**
 * base 视图对象
 *
 * @author dqcer
 * @date 2022/12/08
 */
public class BaseVO extends KeyValueVO<Long, String> {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "BaseVO{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
