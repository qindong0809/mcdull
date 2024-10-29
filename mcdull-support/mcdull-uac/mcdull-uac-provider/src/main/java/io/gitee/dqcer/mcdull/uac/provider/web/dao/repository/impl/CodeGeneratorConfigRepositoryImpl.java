package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.CodeGeneratorConfigMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ICodeGeneratorConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Code generator config repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class CodeGeneratorConfigRepositoryImpl
        extends CrudRepository<CodeGeneratorConfigMapper, CodeGeneratorConfigEntity> implements ICodeGeneratorConfigRepository {

    @Override
    public List<CodeGeneratorConfigEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CodeGeneratorConfigEntity::getId, idList);
        List<CodeGeneratorConfigEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<CodeGeneratorConfigEntity> selectPage(FeedbackQueryDTO param) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> lambda = new QueryWrapper<CodeGeneratorConfigEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(CodeGeneratorConfigEntity::getTableName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    @Override
    public CodeGeneratorConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public void insert(CodeGeneratorConfigEntity entity) {
        baseMapper.insert(entity);
    }


    @Override
    public boolean exist(CodeGeneratorConfigEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<TableColumnVO> getByTable(String table) {
        return baseMapper.getByTable(table);
    }

    @Override
    public List<TableVO> queryTableList(Page<?> page, TableQueryForm dto) {
        return baseMapper.queryTableList(page, dto);
    }

    @Override
    public boolean existByTable(String tableName) {
        return baseMapper.existByTable(tableName) > 0;
    }

    @Override
    public CodeGeneratorConfigEntity getTableConfig(String tableName) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(CodeGeneratorConfigEntity::getTableName, tableName);
        return baseMapper.selectOne(query);
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}