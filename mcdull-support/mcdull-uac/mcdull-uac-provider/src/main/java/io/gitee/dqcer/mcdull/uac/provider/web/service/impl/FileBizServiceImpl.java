package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileBizRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileBizService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FileBizServiceImpl
        extends BasicServiceImpl<IFileBizRepository> implements IFileBizService {

    @Override
    public void save(List<Integer> fileIdList, Integer bizId, String bizCode) {
        if (CollUtil.isNotEmpty(fileIdList) && ObjUtil.isNotNull(bizId)) {
            baseRepository.save(fileIdList, bizId, bizCode);
        }
    }

    @Override
    public Map<Integer, List<Integer>> get(String bizCode) {
        return baseRepository.mapByBizCode(bizCode);
    }


    @Override
    public void remove(Integer bizId, String bizCode) {
        baseRepository.deleteByBizCode(bizId, bizCode);
    }



}
