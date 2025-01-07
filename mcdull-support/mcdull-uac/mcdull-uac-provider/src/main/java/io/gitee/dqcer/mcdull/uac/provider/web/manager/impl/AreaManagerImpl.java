package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IAreaRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IDictTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 *  码表通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/24
 */
@Service
public class AreaManagerImpl implements IAreaManager {

    private static final Logger log = LoggerFactory.getLogger(AreaManagerImpl.class);

    @Resource
    private IAreaRepository areaRepository;


    @Override
    public Map<String, String> map(Set<String> codeSet) {
        if (CollUtil.isNotEmpty(codeSet)) {
            List<AreaEntity> entityList = areaRepository.listByCode(new ArrayList<>(codeSet));
            if (CollUtil.isNotEmpty(entityList)) {
                Map<String, String> map = new HashMap<>(entityList.size());
                for (AreaEntity entity : entityList) {
                    map.put(entity.getCode(), entity.getName());
                }
                return map;
            }
        }
        return Collections.emptyMap();
    }
}
