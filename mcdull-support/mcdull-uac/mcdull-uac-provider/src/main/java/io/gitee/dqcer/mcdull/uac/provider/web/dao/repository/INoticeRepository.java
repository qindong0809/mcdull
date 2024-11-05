package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUserVO;

import java.util.List;

/**
* Notice repository
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeRepository extends IRepository<NoticeEntity> {

    /**
     * 根据id查询
     *
     * @param idList idList
     * @return List<NoticeEntity>
     */
    List<NoticeEntity> queryListByIds(List<Integer> idList);

    /**
     * 分页
     *
     * @param param param
     * @return Page<NoticeEntity>
     */
    Page<NoticeEntity> selectPage(NoticeQueryDTO param);

    /**
     * 查询员工未查看通知
     *
     * @param dto       dto
     * @param userId    userId
     * @param deptIdList deptIdList
     * @param administratorFlag administratorFlag
     * @param deptCode deptCode
     * @param userCode userCode
     * @return Page<NoticeUserVO>
     */
    Page<NoticeUserVO> queryEmployeeNotViewNotice(NoticeEmployeeQueryDTO dto, Integer userId,
                                                  List<Integer> deptIdList,
                                                  Boolean administratorFlag,
                                                  Integer deptCode,
                                                  Integer userCode);

    /**
     * 查询员工通知
     *
     * @param dto       dto
     * @param userId    userId
     * @param deptIdList deptIdList
     * @param administratorFlag administratorFlag
     * @param deptCode deptCode
     * @param userCode userCode
     * @return Page<NoticeUserVO>
     */
    Page<NoticeUserVO> queryEmployeeNotice(NoticeEmployeeQueryDTO dto,
                                           Integer userId,
                                           List<Integer> deptIdList,
                                           Boolean administratorFlag,
                                           Integer deptCode,
                                           Integer userCode);

    /**
     * 按标题名称列出
     *
     * @param title 标题
     * @return {@link List }<{@link NoticeEntity }>
     */
    List<NoticeEntity> listByTitleName(String title);
}