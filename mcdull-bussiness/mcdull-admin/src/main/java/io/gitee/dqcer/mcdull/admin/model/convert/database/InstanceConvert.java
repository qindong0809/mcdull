package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.InstanceVO;

/**
 * 系统配置转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class InstanceConvert {

    public static InstanceVO convertToInstanceVO(InstanceEntity entity) {
        InstanceVO instanceVO = new InstanceVO();
        instanceVO.setId(entity.getId());
        instanceVO.setGroupId(entity.getGroupId());
        instanceVO.setName(entity.getName());
        instanceVO.setHost(entity.getHost());
        instanceVO.setPort(entity.getPort());
        instanceVO.setDatabaseName(entity.getDatabaseName());
        instanceVO.setUsername(entity.getUsername());
        instanceVO.setPassword(entity.getPassword());
        instanceVO.setStatus(entity.getStatus());
        instanceVO.setCreatedTime(entity.getCreatedTime());
        instanceVO.setUpdatedTime(entity.getUpdatedTime());
        instanceVO.setCreatedBy(entity.getCreatedBy());
        instanceVO.setUpdatedBy(entity.getUpdatedBy());
        return instanceVO;
    }

    public static InstanceEntity convertToInstanceDo(InstanceAddDTO dto) {
        InstanceEntity instanceDO = new InstanceEntity();
        instanceDO.setGroupId(dto.getGroupId());
        instanceDO.setName(dto.getName());
        instanceDO.setHost(dto.getHost());
        instanceDO.setPort(dto.getPort());
        instanceDO.setDatabaseName(dto.getDatabaseName());
        instanceDO.setUsername(dto.getUsername());
        instanceDO.setPassword(dto.getPassword());
        instanceDO.setStatus(dto.getStatus());
        return instanceDO;

    }
}
