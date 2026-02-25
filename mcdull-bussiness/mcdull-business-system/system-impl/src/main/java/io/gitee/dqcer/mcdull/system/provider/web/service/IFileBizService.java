package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileBizEntity;

import java.util.List;
import java.util.Map;

/**
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IFileBizService extends IRepository<FileBizEntity> {


    Map<Integer, List<Integer>> get(String bizCode);

    void remove(Integer bizId, String bizCode);

    void save(List<Integer> fileIdList, Integer bizId, String bizCode);

    Map<Integer, List<Integer> > mapByBizCode(String bizCode);

    void deleteByBizCode(Integer bizId, String bizCode);

    void deleteByBizCode(Integer fileId, Integer bizId, String bizCode);

}
