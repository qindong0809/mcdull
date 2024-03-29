package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * id dto
 *
 * @author dqcer
 * @since 2023/02/08 22:02:30
 */
public class IdListDTO<T> implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * id集
     */
    @NotNull
    @Size(min = 1)
    private List<T> ids;

    @Override
    public String toString() {
        return "IdDTO{" + "ids=" + ids +
                '}';
    }

    public void setIds(List<T> ids) {
        this.ids = ids;
    }

    public List<T> getIds() {
        return ids;
    }
}
