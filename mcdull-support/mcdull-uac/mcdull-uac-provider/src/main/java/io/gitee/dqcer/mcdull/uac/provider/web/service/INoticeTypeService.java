package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeTypeVO;

import java.util.List;
import java.util.Map;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeTypeService {

    List<NoticeTypeVO> getAll();

    void add(String name);

    void update(Integer id, String name);

    void delete(Integer id);

    Map<Integer, String> getMap(List<Integer> idList);
}
