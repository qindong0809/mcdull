package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeDO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDictTypeRepository extends IService<DictTypeDO> {

    /**
     * 列表
     *
     * @param dictType 选择类型
     * @return {@link List}<{@link DictTypeDO}>
     */
    List<DictTypeDO> list(String dictType);
}
