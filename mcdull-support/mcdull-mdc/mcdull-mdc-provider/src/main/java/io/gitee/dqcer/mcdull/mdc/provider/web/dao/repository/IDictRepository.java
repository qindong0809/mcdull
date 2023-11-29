package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDictRepository extends IService<DictDO> {

    /**
     * 列表
     *
     * @param selectType 选择类型
     * @return {@link List}<{@link DictDO}>
     */
    List<DictDO> list(String selectType);
}
