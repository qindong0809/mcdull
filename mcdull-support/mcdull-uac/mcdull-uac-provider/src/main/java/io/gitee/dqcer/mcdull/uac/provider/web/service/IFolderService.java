package io.gitee.dqcer.mcdull.uac.provider.web.service;

import cn.hutool.core.lang.Pair;
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

    /**
     * 获取全部
     *
     * @return {@link List }<{@link FolderInfoVO }>
     */
    List<FolderInfoVO> getAll();

    /**
     * 获取系统文件夹 ID
     *
     * @return {@link Pair }<{@link Integer }, {@link String }>
     */
    Integer getSystemFolderId();

    /**
     * 获取根到节点名称
     *
     * @param folderId 文件夹 ID
     * @return {@link String }
     */
    String getRootToNodeName(Integer folderId);

    /**
     * 如果不存在，则添加
     *
     * @param name     名字
     * @param parentId 父 ID
     * @return {@link Integer }
     */
    Integer addIfAbsent(String name, Integer parentId);

    /**
     * 插入
     *
     * @param dto       DTO
     * @param isSaveLog 是保存日志
     * @return {@link Integer }
     */
    Integer insert(FolderInsertDTO dto, boolean isSaveLog);

    /**
     * 更新
     *
     * @param id  id
     * @param dto DTO
     * @return boolean
     */
    boolean update(Integer id, FolderUpdateDTO dto);

    /**
     * 删除
     *
     * @param id id
     * @return boolean
     */
    boolean delete(Integer id);

    /**
     * 获取树
     *
     * @return {@link List }<{@link FolderTreeInfoVO }>
     */
    List<FolderTreeInfoVO> getTree();
}
