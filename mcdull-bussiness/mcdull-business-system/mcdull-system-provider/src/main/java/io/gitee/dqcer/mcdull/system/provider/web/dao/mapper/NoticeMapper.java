package io.gitee.dqcer.mcdull.system.provider.web.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Notice Mapper
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface NoticeMapper extends BaseMapper<NoticeEntity> {

    /**
     * 查询员工未查看通知
     *
     * @param page      page
     * @param dto       dto
     * @param userId    userId
     * @param deptIdList deptIdList
     * @param administratorFlag administratorFlag
     * @param deptCode deptCode
     * @param userCode userCode
     * @return {@link Page}<{@link NoticeUserVO}>
     */
    Page<NoticeUserVO> queryEmployeeNotViewNotice(@Param("page") Page<?> page,
                                                  @Param("query") NoticeEmployeeQueryDTO dto,
                                                  @Param("userId") Integer userId,
                                                  @Param("deptIdList") List<Integer> deptIdList,
                                                  @Param("administratorFlag") Boolean administratorFlag,
                                                  @Param("deptCode") Integer deptCode,
                                                  @Param("userCode") Integer userCode);
    /**
     * 查询员工通知
     *
     * @param page      page
     * @param dto       dto
     * @param userId    userId
     * @param deptIdList deptIdList
     * @param administratorFlag administratorFlag
     * @param deptCode deptCode
     * @param userCode userCode
     * @return {@link Page}<{@link NoticeUserVO}>
     */
    Page<NoticeUserVO> queryEmployeeNotice(@Param("page") Page<?> page,
                                           @Param("query") NoticeEmployeeQueryDTO dto,
                                           @Param("userId") Integer userId,
                                           @Param("deptIdList") List<Integer> deptIdList,
                                           @Param("administratorFlag") Boolean administratorFlag,
                                           @Param("deptCode") Integer deptCode,
                                           @Param("userCode") Integer userCode);

}