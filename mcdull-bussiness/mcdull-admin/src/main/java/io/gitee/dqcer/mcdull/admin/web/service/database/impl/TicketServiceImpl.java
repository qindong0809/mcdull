package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.TicketConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketEditDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupDO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceDO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketDO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketInstanceDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.enums.TicketCancelStatusEnum;
import io.gitee.dqcer.mcdull.admin.model.enums.TicketFollowStatusEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;
import io.gitee.dqcer.mcdull.admin.util.MysqlUtil;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IInstanceRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.ITicketInstanceRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.ITicketRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.ITicketService;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
*  业务实现类
*
* @author dqcer
* @since 2023-08-17
*/
@Service
public class TicketServiceImpl implements ITicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Resource
    private ITicketRepository ticketRepository;

    @Resource
    private ITicketInstanceRepository ticketInstanceRepository;

    @Resource
    private IInstanceRepository instanceRepository;

    @Resource
    private IGroupRepository groupRepository;

    @Resource
    private IUserRepository userRepository;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insert(TicketAddDTO dto) {

        boolean dataExist = this.doCheckDataExist(dto, null);
        if (dataExist) {
            log.warn("数据已存在 dto: {}", dto);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        TicketDO entity = TicketConvert.convertToTicketDO(dto);
        entity.setFollowStatus(TicketFollowStatusEnum.EDIT.getCode());
        entity.setCancelStatus(TicketCancelStatusEnum.OK.getCode());
        String number = buildNumber();
        entity.setNumber(number);
        Long entityId = ticketRepository.insert(entity);

        ticketInstanceRepository.save(entityId, entity.getGroupId(), dto.getInstanceList());

        return Result.ok(entityId);
    }

    private synchronized String buildNumber() {
        List<TicketDO> list = ticketRepository.list();
        String format = String.format("%04d", list.size() + 1);
        return StrUtil.format("NO.{}", format);
    }

    /**
    * 检查数据是否存在
    *
    * @param dto dto
    * @return boolean
    */
    private boolean doCheckDataExist(TicketAddDTO dto, Long id) {
        TicketDO tempEntity = new TicketDO();
        tempEntity.setName(dto.getName());
        if (ObjUtil.isNotNull(id)) {
            tempEntity.setId(id);
        }
        return ticketRepository.exist(tempEntity);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return {@link Result<TicketVO> }
     */
    @Transactional(readOnly = true)
    @Override
    public Result<TicketVO> detail(Long id) {
        TicketDO entity = ticketRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        Map<Long, GroupDO> groupMap = groupRepository.listByIds(ListUtil.of(entity.getGroupId()))
                .stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        Map<Long, UserDO> userMap = userRepository.listByIds(ListUtil.of(entity.getCreatedBy()))
                .stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        return Result.ok(this.buildTicketVO(groupMap, userMap, entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(TicketEditDTO dto) {
        Long id = dto.getId();

        TicketDO dbData = ticketRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        boolean dataExist = this.doCheckDataExist(dto, id);
        if (dataExist) {
            log.warn("数据已存在 dto: {}", dto);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        TicketDO entity = TicketConvert.convertToTicketDO(dto);
        entity.setUpdatedBy(UserContextHolder.currentUserId());
        boolean success = ticketRepository.updateById(entity);
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

        TicketDO dbData = ticketRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        TicketDO entity = new TicketDO();
        entity.setId(id);
//        entity.setStatus(dto.getStatus());
        entity.setUpdatedBy(UserContextHolder.currentUserId());
        boolean success = ticketRepository.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }
    /**
     * 根据主键批量删除
     *
     * @param id id
     * @return {@link Result<Long>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> deleteById(Long id) {
        List<Long> ids = ListUtil.of(id);
        ticketRepository.deleteBatchByIds(ids);
        return Result.ok(id);
    }

    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<List<TicketVO>> queryByIds(List<Long> ids) {
         List<TicketDO> listEntity = ticketRepository.queryListByIds(ids);
         List<TicketVO> voList = new ArrayList<>();
         for (TicketDO entity : listEntity) {
            voList.add(TicketConvert.convertToTicketVO(entity));
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
    public Result<PagedVO<TicketVO>> listByPage(TicketAddDTO dto) {
        Page<TicketDO> entityPage = ticketRepository.selectPage(dto);
        List<TicketVO> voList = new ArrayList<>();
        List<TicketDO> records = entityPage.getRecords();
        Set<Long> groupIdSet = records.stream().map(TicketDO::getGroupId).collect(Collectors.toSet());
        Map<Long, GroupDO> groupMap = groupRepository.listByIds(groupIdSet).stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));

        Set<Long> userIdSet = records.stream().map(BaseDO::getCreatedBy).collect(Collectors.toSet());
        Map<Long, UserDO> userMap = userRepository.listByIds(userIdSet).stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));

        for (TicketDO entity : records) {
            TicketVO ticketVO = this.buildTicketVO(groupMap, userMap, entity);
            voList.add(ticketVO);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    private TicketVO buildTicketVO(Map<Long, GroupDO> groupMap, Map<Long, UserDO> userMap, TicketDO entity) {
        Long groupId = entity.getGroupId();
        List<TicketInstanceDO> ticketInstanceList = ticketInstanceRepository.getListByTicketId(entity.getId());
        List<InstanceDO> repository = instanceRepository.getByGroupId(groupId);

        Map<Long, InstanceDO> instanceMap = repository.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));

        List<SelectOptionVO<Long>> instanceList = new ArrayList<>();
        ticketInstanceList.forEach(i -> {
            Long instanceId = i.getInstanceId();
            InstanceDO instance = instanceMap.get(instanceId);
            instanceList.add(new SelectOptionVO<>(instance.getName(), instance.getId()));
        });

        TicketVO ticketVO = TicketConvert.convertToTicketVO(entity);
        ticketVO.setInstanceSelectOptionVOList(instanceList);
        ticketVO.setGroupName(groupMap.get(groupId).getName());
        ticketVO.setCreatedByStr(userMap.get(entity.getCreatedBy()).getNickName());
        return ticketVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> executeSqlScript(Long id) {
        TicketDO ticketDO = this.beforeCheck(id);
        List<TicketInstanceDO> ticketInstanceDOList = ticketInstanceRepository.getListByTicketId(id);
        List<Long> instanceIdList = ticketInstanceDOList.stream().map(TicketInstanceDO::getInstanceId).collect(Collectors.toList());
        List<InstanceDO> instanceList = instanceRepository.listByIds(instanceIdList);
        instanceList = instanceList.stream().filter(i -> DelFlayEnum.NORMAL.getCode().equals(i.getDelFlag())).collect(Collectors.toList());
        if (CollUtil.isEmpty(instanceList)) {
            return Result.ok();
        }
        this.batchExecute(instanceList, ticketDO.getSqlScript());

        ticketDO.setFollowStatus(TicketFollowStatusEnum.EXECUTED.getCode());
        ticketRepository.updateById(ticketDO);

        return Result.ok(id);
    }

    @SneakyThrows(Exception.class)
    private void batchExecute(List<InstanceDO> instanceList, String sqlScript){
        for (InstanceDO instanceDO : instanceList) {
            Db db = MysqlUtil.getInstance(instanceDO.getHost(), instanceDO.getPort(), instanceDO.getUsername(),
                    instanceDO.getPassword(), instanceDO.getDatabaseName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                    sqlScript.getBytes(StandardCharsets.UTF_8))));
            MysqlUtil.runScript(db, reader);
        }
    }

    private TicketDO beforeCheck(Long id) {
        TicketDO ticketDO = ticketRepository.getById(id);
        Integer followStatus = ticketDO.getFollowStatus();
        if (TicketFollowStatusEnum.PASSED.getCode().equals(followStatus)) {
            Integer cancelStatus = ticketDO.getCancelStatus();
            if (TicketCancelStatusEnum.OK.getCode().equals(cancelStatus)) {
                return ticketDO;
            }
        }
        throw new BusinessException(CodeEnum.NEED_REFRESH_DATA);
    }
}
