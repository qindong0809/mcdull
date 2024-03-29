package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDataEntity;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeEntity;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDictDataRepository extends IService<DictDataEntity> {

    /**
     * 列表
     *
     * @param selectType 选择类型
     * @return {@link List}<{@link DictTypeEntity}>
     */
    List<DictDataEntity> list(String selectType);
}
