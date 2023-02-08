package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.RoleConvert;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IRoleService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.framework.base.dto.IdDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
* 角色表 业务实现类
*
* @author dqcer
* @since 2023-02-08
*/
@Service
public class RoleServiceImpl implements IRoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Resource
    private IRoleRepository roleRepository;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insert(RoleLiteDTO dto) {

        boolean dataExist = this.doCheckDataExist(dto);
        if (dataExist) {
            log.warn("数据已存在 dto: {}", dto);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        RoleDO entity = RoleConvert.convertToRoleDO(dto);
        Long entityId = roleRepository.insert(entity);
        return Result.ok(entityId);
    }

    /**
     * 检查数据是否存在
     *
     * @param dto dto
     * @return boolean
     */
    private boolean doCheckDataExist(RoleLiteDTO dto) {
        RoleDO tempEntity = new RoleDO();
        // TODO: 业务唯一性效验, 除非业务场景不需要
        return roleRepository.exist(tempEntity);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return {@link Result<RoleVO> }
     */
    @Transactional(readOnly = true)
    @Override
    public Result<RoleVO> detail(Long id) {
        RoleDO entity = roleRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(RoleConvert.convertToRoleVO(entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(RoleLiteDTO dto) {
        Long id = dto.getId();

        RoleDO dbData = roleRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        RoleDO entity = RoleConvert.convertToRoleDO(dto);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = roleRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败, entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }

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

        RoleDO dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        RoleDO entity = new RoleDO();
        entity.setId(id);
        entity.setStatus(dto.getStatus());
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = roleRepository.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }
    /**
     * 根据主键删除
     *
     * @param dto 参数
     * @return {@link Result<Long>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<Long>> deleteBatchByIds(IdDTO dto) {
        List<Long> ids = dto.getIds();
        roleRepository.deleteBatchByIds(ids);
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
    public Result<List<RoleVO>> queryByIds(List<Long> ids) {
         List<RoleDO> listEntity = roleRepository.queryListByIds(ids);
         List<RoleVO> voList = new ArrayList<>();
         for (RoleDO entity : listEntity) {
            voList.add(RoleConvert.convertToRoleVO(entity));
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
    public Result<PagedVO<RoleVO>> listByPage(RoleLiteDTO dto) {
        Page<RoleDO> entityPage = roleRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleDO entity : entityPage.getRecords()) {
            voList.add(RoleConvert.convertToRoleVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
