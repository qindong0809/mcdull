package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DictKeyEntity;

import java.util.List;

/**
 * Dict Key Repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDictKeyRepository extends IRepository<DictKeyEntity> {

    /**
     * insert
     *
     * @param entity entity
     */
    void insert(DictKeyEntity entity);

    /**
     * get list all
     *
     * @return {@link List}<{@link DictKeyEntity}>
     */
    List<DictKeyEntity> getListAll();

    /**
     * select page
     *
     * @param dto dto
     * @return {@link Page}<{@link DictKeyEntity}>
     */
    Page<DictKeyEntity> selectPage(DictKeyQueryDTO dto);

    /**
     * insert
     *
     * @param keyCode key code
     * @param keyName key name
     * @param remark  remark
     */
    DictKeyEntity insert(String keyCode, String keyName, String remark);

    /**
     * update
     *
     * @param dictKeyId dict key id
     * @param keyCode   key code
     * @param keyName   key name
     * @param remark    remark
     */
    void update(Integer dictKeyId, String keyCode, String keyName, String remark);
}

