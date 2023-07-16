package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GitAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GitVO;

/**
 * @author dqcer
 * @since 2022/12/24
 */
public class GitConvert {

    public static GitVO convertToGitVO(GitDO entity) {
        GitVO gitVO = new GitVO();
        gitVO.setId(entity.getId());
        gitVO.setName(entity.getName());
        gitVO.setUrl(entity.getUrl());
        gitVO.setUsername(entity.getUsername());
        gitVO.setPassword(entity.getPassword());
        gitVO.setStorePath(entity.getStorePath());
        gitVO.setExecutePath(entity.getExecutePath());
        gitVO.setStatus(entity.getStatus());
        gitVO.setCreatedTime(entity.getCreatedTime());
        return gitVO;
    }

    public static GitDO convertToGitDo(GitAddDTO dto) {
        GitDO gitDO = new GitDO();
        gitDO.setName(dto.getName());
        gitDO.setUrl(dto.getUrl());
        gitDO.setUsername(dto.getUsername());
        gitDO.setPassword(dto.getPassword());
        gitDO.setStorePath(dto.getStorePath());
        gitDO.setExecutePath(dto.getExecutePath());
        gitDO.setStatus(dto.getStatus());
        return gitDO;

    }
}
