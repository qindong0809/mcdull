package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;

import java.util.List;

/**
 * Dict Value Repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDictValueRepository extends IRepository<DictValueEntity> {

    /**
     * insert
     *
     * @param entity entity
     */
    void insert(DictValueEntity entity);

    /**
     * get list by dict key id
     *
     * @param dictKeyId dict key id
     * @return {@link List}<{@link DictValueEntity}>
     */
    List<DictValueEntity> getListByDictKeyId(Integer dictKeyId);

    /**
     * select page
     *
     * @param dto dto
     * @return {@link Page}<{@link DictValueEntity}>
     */
    Page<DictValueEntity> selectPage(DictValueQueryDTO dto);

    /**
     * insert
     *
     * @param dto dto
     */
    DictValueEntity insert(DictValueAddDTO dto);

    /**
     * update
     *
     * @param dto dto
     */
    void update(DictValueUpdateDTO dto);
}

