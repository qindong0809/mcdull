package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeTypeEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeTypeVO;

import java.util.List;
import java.util.Map;

/**
 * Notice Type Service
 *
 * @author dqcer
 * @since 2024/7/25 9:25
 */

public interface INoticeTypeService extends IRepository<NoticeTypeEntity> {

    /**
     * all
     *
     * @return {@link List }<{@link NoticeTypeVO }>
     */
    List<NoticeTypeVO> getAll();

    /**
     * add
     *
     * @param name name
     */
    void add(String name);

    /**
     * update
     *
     * @param id   id
     * @param name name
     */
    void update(Integer id, String name);

    /**
     * delete
     *
     * @param id 身份证
     */
    void delete(Integer id);

    /**
     * map
     *
     * @param idList idList
     * @return {@link Map }<{@link Integer }, {@link String }>
     */
    Map<Integer, String> getMap(List<Integer> idList);
}
