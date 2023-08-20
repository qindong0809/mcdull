package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.GroupConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGroupService;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
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
        Page<GroupDO> entityPage = baseRepository.selectPage(dto);
        for (GroupDO entity : entityPage.getRecords()) {
            voList.add(GroupConvert.convertToGroupVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<GroupVO> detail(Long id) {
        GroupDO entity = baseRepository.getById(id);
        return Result.ok(GroupConvert.convertToGroupVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(GroupAddDTO dto) {
        List<GroupDO> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(null, dto.getName(), list);
        GroupDO sysConfigDO = GroupConvert.convertToGroupDo(dto);
        sysConfigDO.setDelFlag(DelFlayEnum.NORMAL.getCode());
        baseRepository.save(sysConfigDO);
        return Result.ok(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(GroupEditDTO dto) {
        List<GroupDO> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(dto.getId(), dto.getName(), list);
        GroupDO entity = GroupConvert.convertToGroupDo(dto);
        entity.setId(dto.getId());
        baseRepository.updateById(entity);
        return Result.ok(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.ok(id);
    }

    @Override
    public Result<List<GroupVO>> allList() {
        List<GroupVO> voList = getAllList();
        return Result.ok(voList);
    }

    private List<GroupVO> getAllList() {
        List<GroupVO> voList = new ArrayList<>();
        List<GroupDO> list = baseRepository.allList();
        if (CollUtil.isNotEmpty(list)) {
            for (GroupDO groupDO : list) {
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
        return Result.ok(voList);
    }
}
