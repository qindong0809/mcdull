package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserFilterDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserFilterEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserFilterVO;

import java.util.List;

/**
 * user filter
 *
 * @author qin.dong
 * @since  2026/02/09
 */
public interface IUserFilterService extends IRepository<UserFilterEntity> {

    List<UserFilterVO> getList(Integer userId, Integer roleId,String categoryCode, String subCategoryCode);

    boolean delete(Integer id);

    boolean save(Integer userId, Integer roleId, UserFilterDTO dto);
}
