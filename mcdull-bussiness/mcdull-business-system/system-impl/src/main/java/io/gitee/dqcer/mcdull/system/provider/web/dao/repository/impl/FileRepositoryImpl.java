package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FileMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
* File RepositoryImpl
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class FileRepositoryImpl
        extends CrudRepository<FileMapper, FileEntity> implements IFileRepository {

    @Override
    public List<FileEntity> queryListByIds(List<Integer> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            LambdaQueryWrapper<FileEntity> wrapper = Wrappers.lambdaQuery();
            wrapper.in(FileEntity::getId, idList);
            List<FileEntity> list =  baseMapper.selectList(wrapper);
            if (ObjUtil.isNotNull(list)) {
                return list;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Page<FileEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<FileEntity> lambda = new QueryWrapper<FileEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(FileEntity::getFileName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    @Override
    public FileEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    @Override
    public Integer insert(FileEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(FileEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public Page<FileEntity> selectPage(FileQueryDTO dto, List<Integer> userIdList, List<Integer> childerList) {
        LambdaQueryWrapper<FileEntity> lambda = new QueryWrapper<FileEntity>().lambda();
        if (CollUtil.isNotEmpty(userIdList)) {
            lambda.in(BaseEntity::getCreatedBy, userIdList);
        }
        if (CollUtil.isNotEmpty(childerList)) {
            lambda.in(FileEntity::getFolderType, childerList);
        }
        String fileName = dto.getFileName();
        if (StrUtil.isNotBlank(fileName)) {
            lambda.like(FileEntity::getFileName, fileName);
        }
        String fileKey = dto.getFileKey();
        if (StrUtil.isNotBlank(fileKey)) {
            lambda.like(FileEntity::getFileKey, fileKey);
        }
        LocalDate startDate = dto.getCreateTimeBegin();
        LocalDate endDate = dto.getCreateTimeEnd();
        if (ObjUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(RelEntity::getCreatedTime, startDate,
                    LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        String fileType = dto.getFileType();
        if (StrUtil.isNotBlank(fileType)) {
            lambda.like(FileEntity::getFileType, fileType);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public FileEntity getByFileKey(String fileKey) {
        if (StrUtil.isNotBlank(fileKey)) {
            LambdaQueryWrapper<FileEntity> lambda = new QueryWrapper<FileEntity>().lambda();
            lambda.eq(FileEntity::getFileKey, fileKey);
            return baseMapper.selectOne(lambda);
        }

        return null;
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}