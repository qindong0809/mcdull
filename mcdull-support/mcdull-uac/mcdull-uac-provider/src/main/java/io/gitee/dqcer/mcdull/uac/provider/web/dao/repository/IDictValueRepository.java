package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;

import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDictValueRepository extends IService<DictValueEntity> {


    void insert(DictValueEntity entity);

    List<DictValueEntity> getListByDictKeyId(Integer dictKeyId);

    Page<DictValueEntity> selectPage(DictValueQueryDTO dto);

    void insert(DictValueAddDTO dto);

    void update(DictValueUpdateDTO dto);
}

