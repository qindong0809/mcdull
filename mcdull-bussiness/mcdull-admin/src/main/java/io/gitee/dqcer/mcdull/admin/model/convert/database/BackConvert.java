package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.BackAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackListVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackVO;

/**
*  对象转换工具类
*
* @author dqcer
* @since 2023-08-17
*/
public class BackConvert {

    private BackConvert() {
    }

    public static BackListVO toInstanceBackByTicketVO(BackDO entity) {
        BackListVO vo = new BackListVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setModel(entity.getModel());
        vo.setBizId(entity.getBizId());
        vo.setRemark(entity.getRemark());
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setCreatedBy(entity.getCreatedBy());
        return vo;
    }

    public static BackDO toDO(BackAddDTO dto) {
        BackDO backDO = new BackDO();
        backDO.setName(dto.getName());
        backDO.setRemark(dto.getRemark());
        return backDO;
    }

    public static BackVO toBackVO(BackDO back) {
        BackVO backVO = new BackVO();
        backVO.setId(back.getId());
        backVO.setName(back.getName());
        backVO.setTicketId(back.getBizId());
        backVO.setRemark(back.getRemark());
        return backVO;
    }
}