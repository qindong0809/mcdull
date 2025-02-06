package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileBizEntity;

import java.util.List;
import java.util.Map;


/**
 * File biz
 *
 * @author dqcer
 * @since 2025/01/26
 */
public interface IFileBizRepository extends IRepository<FileBizEntity> {

    void save(List<Integer> fileIdList, Integer bizId, String bizCode);

    Map<Integer, List<Integer> > mapByBizCode(String bizCode);

    void deleteByBizCode(Integer bizId, String bizCode);

}