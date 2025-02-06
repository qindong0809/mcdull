package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileBizEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FileBizMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileBizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * File biz 存储库 impl
 *
 * @author dqcer
 * @since 2025/01/26
 */
@Service
public class FileBizRepositoryImpl extends
        CrudRepository<FileBizMapper, FileBizEntity> implements IFileBizRepository {


    @Override
    public void save(List<Integer> fileIdList, Integer bizId, String bizCode) {
        if (ObjUtil.isNotNull(bizId) && StrUtil.isNotBlank(bizCode)) {
            LambdaQueryWrapper<FileBizEntity> query = Wrappers.lambdaQuery();
            query.eq(FileBizEntity::getBizId, bizId);
            query.eq(FileBizEntity::getBizCode, bizCode);
            baseMapper.delete(query);
        }
        if (CollUtil.isNotEmpty(fileIdList) && ObjUtil.isNotNull(bizId) && StrUtil.isNotBlank(bizCode)) {
            List<FileBizEntity> entityList = new ArrayList<>();
            for (Integer fileId : fileIdList) {
                FileBizEntity entity = new FileBizEntity();
                entity.setFileId(fileId);
                entity.setBizId(bizId);
                entity.setBizCode(bizCode);
                entityList.add(entity);
            }
            super.executeBatch(entityList, (sqlSession, entity) -> baseMapper.insert(entity));
        }
    }

    @Override
    public Map<Integer, List<Integer>> mapByBizCode(String bizCode) {
        if (StrUtil.isNotBlank(bizCode)) {
            LambdaQueryWrapper<FileBizEntity> query = Wrappers.lambdaQuery();
            query.eq(FileBizEntity::getBizCode, bizCode);
            List<FileBizEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(
                        Collectors.groupingBy(FileBizEntity::getBizId,
                                Collectors.mapping(FileBizEntity::getFileId, Collectors.toList())));
            }
        }
        return Map.of();
    }

    @Override
    public void deleteByBizCode(Integer bizId, String bizCode) {
        if (ObjUtil.isNotNull(bizId) && StrUtil.isNotBlank(bizCode)) {
            LambdaQueryWrapper<FileBizEntity> query = Wrappers.lambdaQuery();
            query.eq(FileBizEntity::getBizId, bizId);
            query.eq(FileBizEntity::getBizCode, bizCode);
            baseMapper.delete(query);
        }
    }
}
