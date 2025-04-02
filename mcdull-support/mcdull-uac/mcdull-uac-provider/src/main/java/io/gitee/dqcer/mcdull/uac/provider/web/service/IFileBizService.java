package io.gitee.dqcer.mcdull.uac.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IFileBizService {


    void save(List<Integer> fileIdList, Integer bizId, String bizCode);

    Map<Integer, List<Integer>> get(String bizCode);

    void remove(Integer bizId, String bizCode);
}
