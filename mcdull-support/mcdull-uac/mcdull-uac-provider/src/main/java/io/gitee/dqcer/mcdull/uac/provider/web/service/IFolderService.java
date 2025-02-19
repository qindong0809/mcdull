package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.FolderInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FolderUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FolderInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FolderTreeInfoVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * folder service
 *
 * @author dqcer
 * @since 2024/11/29
 */

public interface IFolderService {

    List<FolderInfoVO> getAll();

    @Transactional(rollbackFor = Exception.class)
    Integer getSystemExportFolderId(String menuName);

    Integer insert(FolderInsertDTO dto, boolean isSaveLog);

    boolean update(Integer id, FolderUpdateDTO dto);

    boolean delete(Integer id);

    List<FolderTreeInfoVO> getTree();
}
