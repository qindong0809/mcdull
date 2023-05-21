package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;

import java.util.List;

/**
 * 岗位信息 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IPostRepository extends IService<PostDO> {

    Page<PostDO> selectPage(PostLiteDTO dto);

    boolean checkBusinessUnique(Long id, String postName);

    List<PostDO> getAll();

}
