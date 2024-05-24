package io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dqcer
 * @since 2024-04-29
 */
public interface NoticeMapper extends BaseMapper<NoticeEntity> {

    Page<NoticeUserVO> queryEmployeeNotViewNotice(@Param("page") Page<?> page, @Param("query") NoticeEmployeeQueryDTO dto, @Param("userId") Long userId, @Param("deptIdList") List<Long> deptIdList, @Param("administratorFlag") Boolean administratorFlag, @Param("deptCode") Integer deptCode, @Param("userCode") Integer userCode);
    Page<NoticeUserVO> queryEmployeeNotice(@Param("page") Page<?> page, @Param("query") NoticeEmployeeQueryDTO dto, @Param("userId") Long userId, @Param("deptIdList") List<Long> deptIdList, @Param("administratorFlag") Boolean administratorFlag, @Param("deptCode") Integer deptCode, @Param("userCode") Integer userCode);

}