package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictKeyEntity;

import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDictKeyRepository extends IService<DictKeyEntity> {


    void insert(DictKeyEntity entity);

    List<DictKeyEntity> getListAll();

    Page<DictKeyEntity> selectPage(DictKeyQueryDTO dto);

    void insert(String keyCode, String keyName, String remark);

    void update(Long dictKeyId, String keyCode, String keyName, String remark);
}

