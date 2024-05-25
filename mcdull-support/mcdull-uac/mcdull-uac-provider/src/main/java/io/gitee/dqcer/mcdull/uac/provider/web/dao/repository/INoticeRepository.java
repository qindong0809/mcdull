package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUserVO;

import java.util.List;

/**
* 系统配置 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeRepository extends IService<NoticeEntity> {
    List<NoticeEntity> queryListByIds(List<Integer> idList);

    Page<NoticeEntity> selectPage(NoticeQueryDTO param);

    Page<NoticeUserVO> queryEmployeeNotViewNotice(NoticeEmployeeQueryDTO dto, Integer userId,
                                                  List<Integer> deptIdList,
                                                  Boolean administratorFlag,
                                                  Integer deptCode,
                                                  Integer userCode);

    Page<NoticeUserVO> queryEmployeeNotice(NoticeEmployeeQueryDTO dto,
                                           Integer userId,
                                           List<Integer> deptIdList,
                                           Boolean administratorFlag,
                                           Integer deptCode,
                                           Integer userCode);
}