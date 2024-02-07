package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IMenuService {
    Map<Long, List<String>> getMenuCodeListMap(List<Long> roleIdList);

    List<String> getAllCodeList();
}
