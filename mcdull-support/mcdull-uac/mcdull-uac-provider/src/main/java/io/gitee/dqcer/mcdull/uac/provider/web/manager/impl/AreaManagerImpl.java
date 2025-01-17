package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.IArea;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IAreaRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public <T extends IArea> void set(List<T> list) {
        if (CollUtil.isNotEmpty(list)) {
            Set<String> provincesSet = list.stream().map(IArea::getProvincesCode).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            Set<String> citySet = list.stream().map(IArea::getCityCode).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            Set<String> codList = new HashSet<>(provincesSet.size() + citySet.size());
            if (CollUtil.isNotEmpty(provincesSet)) {
                codList.addAll(provincesSet);
            }
            if (CollUtil.isNotEmpty(citySet)) {
                codList.addAll(citySet);
            }
            Map<String, String> map = this.map(codList);
            for (T t : list) {
                if (ObjUtil.isNotNull(t.getProvincesCode())) {
                    t.setProvincesName(map.get(t.getProvincesCode()));
                }
                if (ObjUtil.isNotNull(t.getCityCode())) {
                    t.setCityName(map.get(t.getCityCode()));
                }
            }
        }
    }
}
