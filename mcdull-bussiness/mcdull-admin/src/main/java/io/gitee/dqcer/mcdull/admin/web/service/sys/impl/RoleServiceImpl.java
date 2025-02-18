package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.RoleConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleUpdateDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleEntity;
import io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.admin.util.LogHelpUtil;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IRoleService;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
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


    @Resource
    private IMenuRepository menuRepository;

    /**
     * 分页列表
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<PagedVO<RoleVO>> listByPage(RoleLiteDTO dto) {
        Page<RoleEntity> entityPage = roleRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleEntity entity : entityPage.getRecords()) {
            voList.add(RoleConvert.convertToRoleVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }


    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insert(RoleInsertDTO dto) {

        boolean dataExist = this.doCheckDataExist(dto.getName());
        if (dataExist) {
            log.warn("数据已存在 dto: {}", dto);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        RoleEntity entity = RoleConvert.convertToRoleDO(dto);
        entity.setType(UserTypeEnum.READ_WRITE.getCode());
        Long entityId = roleRepository.insert(entity);

        roleRepository.batchSaveMenu(entityId, dto.getMenuIds());

        this.buildAddOrEditLog(dto);

        return Result.success(entityId);
    }

    private void buildAddOrEditLog(RoleInsertDTO dto) {
        String logDesc = "角色:{}";
        LogHelpUtil.setLog(StrUtil.format(logDesc, dto.getName()));
    }

    private boolean doCheckDataExist(String name) {
        RoleEntity tempEntity = new RoleEntity();
        tempEntity.setName(name);
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
        RoleEntity entity = roleRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        return Result.success(RoleConvert.convertToRoleVO(entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(RoleUpdateDTO dto) {
        Long id = dto.getRoleId();

        RoleEntity dbData = roleRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        RoleEntity entity = RoleConvert.convertToRoleDO(dto);
        entity.setId(id);
;
        boolean success = roleRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败, entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }

        roleRepository.batchUpdateMenu(id, dto.getMenuIds());
        this.buildAddOrEditLog(dto);
        return Result.success(id);
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

        RoleEntity dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(id);
        entity.setStatus(dto.getStatus());

        boolean success = roleRepository.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.success(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> deleteById(Long roleId) {
        roleRepository.deleteBatchByIds(ListUtil.of(roleId));
        return Result.success(roleId);
    }


}
