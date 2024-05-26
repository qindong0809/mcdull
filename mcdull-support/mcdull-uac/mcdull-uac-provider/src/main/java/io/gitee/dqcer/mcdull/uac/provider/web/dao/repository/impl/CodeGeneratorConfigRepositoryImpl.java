package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.CodeGeneratorConfigMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ICodeGeneratorConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class CodeGeneratorConfigRepositoryImpl extends ServiceImpl<CodeGeneratorConfigMapper, CodeGeneratorConfigEntity>  implements ICodeGeneratorConfigRepository {

    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorConfigRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< CodeGeneratorConfigEntity >}
     */
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

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page< CodeGeneratorConfigEntity >}
     */
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

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link CodeGeneratorConfigEntity}
     */
    @Override
    public CodeGeneratorConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    @Override
    public void insert(CodeGeneratorConfigEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
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
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}