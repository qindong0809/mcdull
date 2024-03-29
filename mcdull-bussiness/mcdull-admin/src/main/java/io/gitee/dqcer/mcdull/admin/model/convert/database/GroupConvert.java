package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;

/**
 * 系统配置转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class GroupConvert {

    public static GroupVO convertToGroupVO(GroupEntity entity) {
        GroupVO groupVO = new GroupVO();
        groupVO.setId(entity.getId());
        groupVO.setName(entity.getName());
        groupVO.setStatus(entity.getStatus());
        groupVO.setCreatedTime(entity.getCreatedTime());
        return groupVO;

    }

    public static GroupEntity convertToGroupDo(GroupAddDTO dto) {
        GroupEntity groupDO = new GroupEntity();
        groupDO.setName(dto.getName());
        groupDO.setStatus(dto.getStatus());
        return groupDO;
    }
}
