package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.FolderInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FolderUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FolderInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FolderTreeInfoVO;

import java.util.List;

/**
 * folder service
 *
 * @author dqcer
 * @since 2024/11/29
 */

public interface IFolderService {

    List<FolderInfoVO> getAll();

    boolean insert(FolderInsertDTO dto);

    boolean update(Integer id, FolderUpdateDTO dto);

    boolean delete(Integer id);

    List<FolderTreeInfoVO> getTree();
}
