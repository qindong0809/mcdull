package ${package.ServiceImpl};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.apiConvert}.${cfg.convertName};
import ${package.Service}.${cfg.serviceName};
import ${cfg.apiEntity}.${cfg.entityName};
import ${cfg.apiVo}.${cfg.voName};
import ${cfg.repository}.${cfg.repositoryName};
import ${cfg.StatusDTO};
import ${cfg.UserContextHolder};
import ${cfg.DatabaseRowException};
import ${cfg.PageUtil};
import ${cfg.PagedVO};
import ${cfg.CodeEnum};
import ${cfg.result};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
* ${table.comment!} 业务实现类
*
* @author ${author}
* @since ${date}
*/
@Service
public class ${cfg.serviceImplName} implements ${cfg.serviceName} {

    private static final Logger log = LoggerFactory.getLogger(${cfg.serviceImplName}.class);

    @Resource
    private ${cfg.repositoryName} ${(cfg.repositoryName?substring(1))?uncap_first};

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insert(${cfg.dtoName} dto) {

        ${cfg.entityName} tempEntity = new ${cfg.entityName}();
        // TODO: 业务唯一性效验, 除非业务场景不需要
        boolean exist = ${(cfg.repositoryName?substring(1))?uncap_first}.exist(tempEntity);
        if (exist) {
            log.warn("数据已存在 query:{}", tempEntity);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        ${cfg.entityName} entity = ${cfg.convertName}.convertTo${cfg.entityName}(dto);
        Long entityId = ${(cfg.repositoryName?substring(1))?uncap_first}.insert(entity);
        return Result.ok(entityId);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return {@link Result<${cfg.voName}> }
     */
    @Transactional(readOnly = true)
    @Override
    public Result<${cfg.voName}> detail(Long id) {
        ${cfg.entityName} entity = ${(cfg.repositoryName?substring(1))?uncap_first}.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(${cfg.convertName}.convertTo${cfg.voName}(entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(${cfg.dtoName} dto) {
        Long id = dto.getId();

        ${cfg.entityName} dbData = ${(cfg.repositoryName?substring(1))?uncap_first}.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        ${cfg.entityName} entity = ${cfg.convertName}.convertTo${cfg.entityName}(dto);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = ${(cfg.repositoryName?substring(1))?uncap_first}.updateById(entity);
        if (!success) {
            log.error("数据更新失败, entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }

<#list table.fields as field>
<#if "status" == field.name>
    /**
     * 更新状态
     *
     * @param dto    参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> updateStatus(StatusDTO dto) {
        Long id = dto.getId();

        ${cfg.entityName} dbData = ${(cfg.repositoryName?substring(1))?uncap_first}.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        ${cfg.entityName} entity = new ${cfg.entityName}();
        entity.setId(id);
        entity.setStatus(dto.getStatus());
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = ${(cfg.repositoryName?substring(1))?uncap_first}.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }
</#if>
</#list>
    /**
     * 根据主键删除
     *
     * @param dto 参数
     * @return {@link Result<Long>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> delete(${cfg.dtoName} dto) {
        Long id = dto.getId();
        ${(cfg.repositoryName?substring(1))?uncap_first}.deleteById(dto.getId());
        return Result.ok(id);
    }

    /**
     * 根据主键集删除
     *
     * @param ids id集
     * @return {@link Result<List>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<Long>> deleteByIds(List<Long> ids) {
        ${(cfg.repositoryName?substring(1))?uncap_first}.deleteBatchIds(ids);
        return Result.ok(ids);
    }

    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<List<${cfg.voName}>> queryByIds(List<Long> ids) {
         List<${cfg.entityName}> listEntity = ${(cfg.repositoryName?substring(1))?uncap_first}.queryListByIds(ids);
         List<${cfg.voName}> voList = new ArrayList<>();
         for (${cfg.entityName} entity : listEntity) {
            voList.add(${cfg.convertName}.convertTo${cfg.voName}(entity));
         }
         return Result.ok(voList);
    }

    /**
     * 分页列表
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<PagedVO<${cfg.voName}>> listByPage(${cfg.dtoName} dto) {
        Page<${cfg.entityName}> entityPage = ${(cfg.repositoryName?substring(1))?uncap_first}.selectPage(dto);
        List<${cfg.voName}> voList = new ArrayList<>();
        for (${cfg.entityName} entity : entityPage.getRecords()) {
            voList.add(${cfg.convertName}.convertTo${cfg.voName}(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
