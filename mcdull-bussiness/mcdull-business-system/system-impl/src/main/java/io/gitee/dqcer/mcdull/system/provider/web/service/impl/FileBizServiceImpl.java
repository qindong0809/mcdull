package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileBizEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FileBizMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileBizService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FileBizServiceImpl
        extends BasicCurdServiceImpl<FileBizMapper, FileBizEntity> implements IFileBizService {

    @Override
    public Map<Integer, List<Integer>> get(String bizCode) {
        return this.mapByBizCode(bizCode);
    }


    @Override
    public void remove(Integer bizId, String bizCode) {
        this.deleteByBizCode(bizId, bizCode);
    }



    @Override
    public void save(List<Integer> fileIdList, Integer bizId, String bizCode) {
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

    @Override
    public void deleteByBizCode(Integer fileId, Integer bizId, String bizCode) {
        if (ObjUtil.isNotNull(fileId) && ObjUtil.isNotNull(bizId) && StrUtil.isNotBlank(bizCode)) {
            LambdaQueryWrapper<FileBizEntity> query = Wrappers.lambdaQuery();
            query.eq(FileBizEntity::getFileId, fileId);
            query.eq(FileBizEntity::getBizId, bizId);
            query.eq(FileBizEntity::getBizCode, bizCode);
            baseMapper.delete(query);
        }
    }

}
