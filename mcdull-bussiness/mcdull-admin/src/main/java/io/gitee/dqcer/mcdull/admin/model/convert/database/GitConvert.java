package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GitAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GitVO;

/**
 * @author dqcer
 * @since 2022/12/24
 */
public class GitConvert {

    public static GitVO convertToGitVO(GitEntity entity) {
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

    public static GitEntity convertToGitDo(GitAddDTO dto) {
        GitEntity gitDO = new GitEntity();
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
