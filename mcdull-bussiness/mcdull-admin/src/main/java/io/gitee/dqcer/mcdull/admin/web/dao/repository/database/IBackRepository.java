package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.database.BackListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackDO;

import java.util.List;

/**
* back 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IBackRepository extends IService<BackDO>  {
    List<BackDO> listByBizId(Long ticketId, Integer modelTicket);

    Page<BackDO> selectPage(BackListDTO dto);

    void updateToDelete(Long id);
}