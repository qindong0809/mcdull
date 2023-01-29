package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeDO;

/**
* 通知公告表 对象转换工具类
*
* @author dqcer
* @since 2023-01-18
*/
public class NoticeConvert {

   /**
    * NoticeLiteDTO转换为NoticeDO
    *
    * @param item NoticeLiteDTO
    * @return {@link NoticeDO}
    */
    public static NoticeDO convertToNoticeDO(NoticeLiteDTO item){
        if (item == null){
            return null;
        }
        NoticeDO entity = new NoticeDO();
        entity.setId(item.getId());
        entity.setTitle(item.getTitle());
        entity.setContent(item.getContent());
        entity.setType(item.getType());
        entity.setStatus(item.getStatus());

        return entity;
    }


    /**
    * NoticeDONoticeVO
    *
    * @param item NoticeDO
    * @return {@link NoticeVO}
    */
    public static NoticeVO convertToNoticeVO(NoticeDO item){
        if (item == null){
            return null;
        }
        NoticeVO vo = new NoticeVO();
        vo.setId(item.getId());
        vo.setTitle(item.getTitle());
        vo.setContent(item.getContent());
        vo.setType(item.getType());
        vo.setStatus(item.getStatus());

        return vo;
    }
    private NoticeConvert() {
    }
}