package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;

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
       NoticeDO noticeDO = new NoticeDO();
       noticeDO.setNoticeTitle(item.getNoticeTitle());
       noticeDO.setNoticeContent(item.getNoticeContent());
       noticeDO.setNoticeType(item.getNoticeType());
       noticeDO.setRemark(item.getRemark());
       noticeDO.setStatus(item.getStatus());
       noticeDO.setId(item.getId());
       return noticeDO;
    }


    /**
    * NoticeDONoticeVO
    *
    * @param item NoticeDO
    * @return {@link NoticeVO}
    */
    public static NoticeVO convertToNoticeVO(NoticeDO item){
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setId(item.getId());
        noticeVO.setNoticeTitle(item.getNoticeTitle());
        noticeVO.setNoticeContent(item.getNoticeContent());
        noticeVO.setNoticeType(item.getNoticeType());
        noticeVO.setRemark(item.getRemark());
        noticeVO.setStatus(item.getStatus());
        noticeVO.setCreatedTime(item.getCreatedTime());
        noticeVO.setCreatedBy(item.getCreatedBy());
        return noticeVO;

    }
    private NoticeConvert() {
    }
}