package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.vo.RouterVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IMenuService {
    Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList);

    List<String> getAllCodeList();

    List<RouterVO> allTree();

    List<RouterVO> tree(Integer userId);
}
