package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.InstanceConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupEntity;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.InstanceVO;
import io.gitee.dqcer.mcdull.admin.util.MysqlUtil;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IInstanceRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IInstanceService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Instance 服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class InstanceServiceImpl extends BasicServiceImpl<IInstanceRepository> implements IInstanceService {

    @Resource
    IGroupRepository groupRepository;

    @Override
    public Result<PagedVO<InstanceVO>> list(InstanceListDTO dto) {
        List<InstanceVO> voList = new ArrayList<>();
        Page<InstanceEntity> entityPage = baseRepository.selectPage(dto);
        List<GroupEntity> groupList = groupRepository.allList();

        for (InstanceEntity entity : entityPage.getRecords()) {
            InstanceVO vo = InstanceConvert.convertToInstanceVO(entity);
            Optional<GroupEntity> first = groupList.stream().filter(i -> i.getId().equals(entity.getGroupId())).findFirst();
            first.ifPresent(groupDO -> vo.setGroupName(groupDO.getName()));
            voList.add(vo);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<InstanceVO> detail(Long id) {
        InstanceEntity entity = baseRepository.getById(id);
        return Result.success(InstanceConvert.convertToInstanceVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(InstanceAddDTO dto) {
        List<InstanceEntity> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(null, dto.getName(), list);
        InstanceEntity sysConfigDO = InstanceConvert.convertToInstanceDo(dto);
        baseRepository.save(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(InstanceEditDTO dto) {
        List<InstanceEntity> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(dto.getId(), dto.getName(), list);
        InstanceEntity sysConfigDO = InstanceConvert.convertToInstanceDo(dto);
        sysConfigDO.setId(dto.getId());
        baseRepository.updateById(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.success(id);
    }

    @Override
    public Result<List<SelectOptionVO<Long>>> baseInfoListByGroupId(Long groupId) {
        List<SelectOptionVO<Long>> voList = new ArrayList<>();
        List<InstanceEntity> list = baseRepository.getByGroupId(groupId);
        for (InstanceEntity instanceDO : list) {
            voList.add(new SelectOptionVO<>(instanceDO.getName(), instanceDO.getId()));
        }
        return Result.success(voList);
    }

    @SneakyThrows(Exception.class)
    @Override
    public Result<Boolean> testConnect(InstanceAddDTO dto) {
        boolean isOk = MysqlUtil.testConnect(dto.getHost(), dto.getPort(), dto.getUsername(), dto.getPassword(), dto.getDatabaseName());
        return Result.success(isOk);
    }

    @Override
    public Result<List<SelectOptionVO<Long>>> backList() {
        // TODO: 2023/8/24 备份列表 
        return null;
    }

}
