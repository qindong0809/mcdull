package io.gitee.dqcer.mcdull.system.provider.web.service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IFileBizService {


    Map<Integer, List<Integer>> get(String bizCode);

    void remove(Integer bizId, String bizCode);
}
