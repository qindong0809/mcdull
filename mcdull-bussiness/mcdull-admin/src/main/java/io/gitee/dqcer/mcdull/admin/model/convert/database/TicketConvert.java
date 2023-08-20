package io.gitee.dqcer.mcdull.admin.model.convert.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketEditDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;

/**
*  对象转换工具类
*
* @author dqcer
* @since 2023-08-17
*/
public class TicketConvert {

   /**
    * TicketLiteDTO转换为TicketDO
    *
    * @param item TicketLiteDTO
    * @return {@link TicketDO}
    */
    public static TicketDO convertToTicketDO(TicketAddDTO item){
        if (item == null){
            return null;
        }
        TicketDO entity = new TicketDO();
        entity.setName(item.getName());
        entity.setGroupId(item.getGroupId());
        entity.setExecuteType(item.getExecuteType());
        entity.setHasMerge(item.getHasMerge());
        entity.setRemark(item.getRemark());
        entity.setSqlScript(item.getSqlScript());

        return entity;
    }

    public static TicketDO convertToTicketDO(TicketEditDTO item){
        TicketDO ticketDO = convertToTicketDO((TicketAddDTO)item);
        ticketDO.setId(item.getId());
        return ticketDO;
    }



    /**
    * TicketDOTicketVO
    *
    * @param item TicketDO
    * @return {@link TicketVO}
    */
    public static TicketVO convertToTicketVO(TicketDO item){
        if (item == null){
            return null;
        }
        TicketVO vo = new TicketVO();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setNumber(item.getNumber());
        vo.setGroupId(item.getGroupId());
        vo.setExecuteType(item.getExecuteType());
        vo.setHasMerge(item.getHasMerge());
        vo.setRemark(item.getRemark());
        vo.setSqlScript(item.getSqlScript());
        vo.setFollowStatus(item.getFollowStatus());
        vo.setCancelStatus(item.getCancelStatus());
        vo.setCreatedTime(item.getCreatedTime());

        return vo;
    }
    private TicketConvert() {
    }
}