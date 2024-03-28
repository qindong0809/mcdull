package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDataDO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeDO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDictDataRepository extends IService<DictDataDO> {

    /**
     * 列表
     *
     * @param selectType 选择类型
     * @return {@link List}<{@link DictTypeDO}>
     */
    List<DictDataDO> list(String selectType);
}
