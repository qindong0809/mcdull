package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeVO;

import java.util.List;

/**
 * Notice Service
 *
 * @author dqcer
 * @since 2024/7/25 9:25
 */

public interface INoticeService extends IRepository<NoticeEntity> {

    /**
     * insert
     *
     * @param dto DTO
     */
    void insert(NoticeAddDTO dto);

    /**
     * update
     *
     * @param dto DTO
     */
    void update(NoticeUpdateDTO dto);

    /**
     * detail
     *
     * @param id id
     * @return {@link NoticeVO }
     */
    NoticeVO detail(Integer id);

    /**
     * batch delete
     *
     * @param idList idList
     */
    void batchDelete(List<Integer> idList);

    /**
     * query page
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link NoticeVO }>
     */
    PagedVO<NoticeVO> queryPage(NoticeQueryDTO dto);

    /**
     * get update form
     *
     * @param noticeId noticeId
     * @return {@link NoticeUpdateFormVO }
     */
    NoticeUpdateFormVO getUpdateFormVO(Integer noticeId);

    /**
     * query user notice
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link NoticeUserVO }>
     */
    PagedVO<NoticeUserVO> queryUserNotice(NoticeEmployeeQueryDTO dto);

    /**
     * view
     *
     * @param noticeId noticeId
     * @return {@link NoticeDetailVO }
     */
    NoticeDetailVO view(Integer noticeId);

    /**
     * export data
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(NoticeQueryDTO dto);
}
