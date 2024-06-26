package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.IdName;

import java.util.StringJoiner;

/**
 * base 视图对象
 *
 * @author dqcer
 * @since 2022/12/08
 */
public class BaseVO<ID, Name> implements IdName<ID, Name> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    protected ID id;

    /**
     * name
     */
    protected Name name;

    public BaseVO() {
    }

    public BaseVO(ID id, Name name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseVO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name=" + name)
                .toString();
    }

    @Override
    public ID getId() {
        return id;
    }

    public BaseVO<ID, Name> setId(ID id) {
        this.id = id;
        return this;
    }

    @Override
    public Name getName() {
        return name;
    }

    public BaseVO<ID, Name> setName(Name name) {
        this.name = name;
        return this;
    }
}
