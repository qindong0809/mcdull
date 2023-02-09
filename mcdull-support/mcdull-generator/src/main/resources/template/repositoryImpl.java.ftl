package ${cfg.repositoryImpl};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.apiEntity}.${cfg.entityName};
import ${package.Mapper}.${cfg.mapperName};
import ${cfg.repository}.${cfg.repositoryName};
import ${cfg.GlobalConstant};
import ${cfg.DelFlayEnum};
import ${cfg.DatabaseRowException};
import ${cfg.ObjUtil};
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* ${table.comment!} 数据库操作封装实现层
*
* @author ${author}
* @since ${date}
*/
@Service
public class ${cfg.repositoryImplName} extends ServiceImpl<${cfg.mapperName}, ${cfg.entityName}>  implements ${cfg.repositoryName} {

    private static final Logger log = LoggerFactory.getLogger(${cfg.repositoryImplName}.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List<${cfg.entityName}>}
     */
    @Override
    public List<${cfg.entityName}> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<${cfg.entityName}> wrapper = Wrappers.lambdaQuery();
        wrapper.in(${cfg.entityName}::getId, idList);
        wrapper.eq(${cfg.entityName}::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<${cfg.entityName}> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page<${cfg.entityName}>}
     */
    @Override
    public Page<${cfg.entityName}> selectPage(${cfg.dtoName} param) {
        LambdaQueryWrapper<${cfg.entityName}> lambda = new QueryWrapper<${cfg.entityName}>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        return baseMapper.selectPage(new Page<>(param.getPage(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ${cfg.entityName}}
     */
    @Override
    public ${cfg.entityName} getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(${cfg.entityName} entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    @Override
    public void deleteById(Long id) {
        int rowSize = baseMapper.deleteById(id);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, id:{}", rowSize, id);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 批量删除
     *
     * @param ids 主键集
     * @return int 受影响的行数
     */
    @Override
    public void deleteBatchIds(List<Long> ids) {
         return baseMapper.deleteBatchIds(ids);
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(${cfg.entityName} entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}