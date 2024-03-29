package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.GroupConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGroupService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance 服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class GroupServiceImpl extends BasicServiceImpl<IGroupRepository> implements IGroupService {


    @Override
    public Result<PagedVO<GroupVO>> list(GroupListDTO dto) {
        List<GroupVO> voList = new ArrayList<>();
        Page<GroupEntity> entityPage = baseRepository.selectPage(dto);
        for (GroupEntity entity : entityPage.getRecords()) {
            voList.add(GroupConvert.convertToGroupVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<GroupVO> detail(Long id) {
        GroupEntity entity = baseRepository.getById(id);
        return Result.success(GroupConvert.convertToGroupVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(GroupAddDTO dto) {
        List<GroupEntity> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(null, dto.getName(), list);
        GroupEntity sysConfigDO = GroupConvert.convertToGroupDo(dto);
        baseRepository.save(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(GroupEditDTO dto) {
        List<GroupEntity> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(dto.getId(), dto.getName(), list);
        GroupEntity entity = GroupConvert.convertToGroupDo(dto);
        entity.setId(dto.getId());
        baseRepository.updateById(entity);
        return Result.success(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.success(id);
    }

    @Override
    public Result<List<GroupVO>> allList() {
        List<GroupVO> voList = getAllList();
        return Result.success(voList);
    }

    private List<GroupVO> getAllList() {
        List<GroupVO> voList = new ArrayList<>();
        List<GroupEntity> list = baseRepository.allList();
        if (CollUtil.isNotEmpty(list)) {
            for (GroupEntity groupDO : list) {
                GroupVO vo = GroupConvert.convertToGroupVO(groupDO);
                voList.add(vo);
            }
        }
        return voList;
    }


    @Override
    public Result<List<SelectOptionVO<Long>>> baseInfoList() {
        List<SelectOptionVO<Long>> voList = new ArrayList<>();
        List<GroupVO> list = getAllList();
        for (GroupVO group : list) {
            voList.add(new SelectOptionVO<>(group.getName(), group.getId()));
        }
        return Result.success(voList);
    }
}
