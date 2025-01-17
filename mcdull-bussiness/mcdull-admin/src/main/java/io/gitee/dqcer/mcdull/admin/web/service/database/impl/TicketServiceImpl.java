package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.db.Db;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.BackConvert;
import io.gitee.dqcer.mcdull.admin.model.convert.database.TicketConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.*;
import io.gitee.dqcer.mcdull.admin.model.entity.database.*;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserEntity;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.model.enums.TicketCancelStatusEnum;
import io.gitee.dqcer.mcdull.admin.model.enums.TicketFollowStatusEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackListVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;
import io.gitee.dqcer.mcdull.admin.util.MysqlUtil;
import io.gitee.dqcer.mcdull.admin.util.dump.SqlDumper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.*;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.service.database.ITicketService;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
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

import jakarta.annotation.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    @Resource
    private ISysConfigManager sysConfigManager;

    @Resource
    private IBackInstanceRepository backInstanceRepository;

    @Resource
    private IBackRepository backRepository;

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

        TicketEntity entity = TicketConvert.convertToTicketDO(dto);
        entity.setFollowStatus(TicketFollowStatusEnum.EDIT.getCode());
        entity.setCancelStatus(TicketCancelStatusEnum.OK.getCode());
        String number = buildNumber();
        entity.setNumber(number);
        Long entityId = ticketRepository.insert(entity);

        ticketInstanceRepository.save(entityId, entity.getGroupId(), dto.getInstanceList());

        return Result.success(entityId);
    }

    private synchronized String buildNumber() {
        List<TicketEntity> list = ticketRepository.list();
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
        TicketEntity tempEntity = new TicketEntity();
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
        TicketEntity entity = ticketRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        Map<Long, GroupEntity> groupMap = groupRepository.listByIds(ListUtil.of(entity.getGroupId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        Map<Long, UserEntity> userMap = userRepository.listByIds(ListUtil.of(entity.getCreatedBy()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        return Result.success(this.buildTicketVO(groupMap, userMap, entity));
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

        TicketEntity dbData = ticketRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        boolean dataExist = this.doCheckDataExist(dto, id);
        if (dataExist) {
            log.warn("数据已存在 dto: {}", dto);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        TicketEntity entity = TicketConvert.convertToTicketDO(dto);

        boolean success = ticketRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败, entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
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
    public Result<Long> updateStatus(TicketFollowStatusDTO dto) {
        Long id = dto.getId();

        TicketEntity dbData = ticketRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        Integer status = dto.getStatus();
        if (!((TicketFollowStatusEnum.EDIT.getCode().equals(dbData.getFollowStatus()) && TicketFollowStatusEnum.PUBLISHED.getCode().equals(status))
                || (TicketFollowStatusEnum.PUBLISHED.getCode().equals(dbData.getFollowStatus()) && TicketFollowStatusEnum.PASSED.getCode().equals(status))
                || (TicketFollowStatusEnum.PASSED.getCode().equals(dbData.getFollowStatus()) && TicketFollowStatusEnum.EXECUTED.getCode().equals(status)))
        ) {
            log.error("数据异常 id:{}", id);
            return Result.error(CodeEnum.ERROR_PARAMETERS, Collections.singletonList(dbData.getFollowStatus().toString()));
        }

        TicketEntity entity = new TicketEntity();
        entity.setId(id);
        entity.setFollowStatus(status);

        boolean success = ticketRepository.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.success(id);
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
        return Result.success(id);
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
         List<TicketEntity> listEntity = ticketRepository.queryListByIds(ids);
         List<TicketVO> voList = new ArrayList<>();
         for (TicketEntity entity : listEntity) {
            voList.add(TicketConvert.convertToTicketVO(entity));
         }
         return Result.success(voList);
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
        Page<TicketEntity> entityPage = ticketRepository.selectPage(dto);
        List<TicketVO> voList = new ArrayList<>();
        List<TicketEntity> records = entityPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(PageUtil.toPage(voList, entityPage));
        }

        Set<Long> groupIdSet = records.stream().map(TicketEntity::getGroupId).collect(Collectors.toSet());
        Map<Long, GroupEntity> groupMap = groupRepository.listByIds(groupIdSet).stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        Set<Long> userIdSet = records.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
        Map<Long, UserEntity> userMap = userRepository.listByIds(userIdSet).stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        for (TicketEntity entity : records) {
            TicketVO ticketVO = this.buildTicketVO(groupMap, userMap, entity);
            voList.add(ticketVO);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    private TicketVO buildTicketVO(Map<Long, GroupEntity> groupMap, Map<Long, UserEntity> userMap, TicketEntity entity) {
        Long groupId = entity.getGroupId();
        List<TicketInstanceEntity> ticketInstanceList = ticketInstanceRepository.getListByTicketId(entity.getId());
        List<InstanceEntity> repository = instanceRepository.getByGroupId(groupId);

        Map<Long, InstanceEntity> instanceMap = repository.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        List<SelectOptionVO<Long>> instanceList = new ArrayList<>();
        ticketInstanceList.forEach(i -> {
            Long instanceId = i.getInstanceId();
            InstanceEntity instance = instanceMap.get(instanceId);
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
        TicketEntity ticketDO = this.beforeCheck(id);
        List<TicketInstanceEntity> ticketInstanceDOList = ticketInstanceRepository.getListByTicketId(id);
        List<Long> instanceIdList = ticketInstanceDOList.stream().map(TicketInstanceEntity::getInstanceId).collect(Collectors.toList());
        List<InstanceEntity> instanceList = instanceRepository.listByIds(instanceIdList);
        if (CollUtil.isEmpty(instanceList)) {
            return Result.success();
        }
        this.batchExecute(instanceList, ticketDO.getSqlScript());

        ticketDO.setFollowStatus(TicketFollowStatusEnum.EXECUTED.getCode());
        ticketRepository.updateById(ticketDO);

        return Result.success(id);
    }

    private Map<Long, InstanceEntity> getInstanceMap(Long id) {
        List<TicketInstanceEntity> ticketInstanceList = ticketInstanceRepository.getListByTicketId(id);
        List<InstanceEntity> instanceList = instanceRepository.listByIds(ticketInstanceList.stream().map(TicketInstanceEntity::getInstanceId).collect(Collectors.toSet()));
        return instanceList.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> rollbackByTicket(Long backId) {
        BackEntity back = backRepository.getById(backId);
        Long ticketId = back.getBizId();
        Map<Long, InstanceEntity> instanceMap = this.getInstanceMap(ticketId);
        String sqlDumpDir = sysConfigManager.findValueByEnum(SysConfigKeyEnum.DATABASE_SQL_DUMP);
        List<BackInstanceEntity> instanceBackList = backInstanceRepository.listByBackId(backId);
        Map<Long, BackInstanceEntity> instanceBackMap = instanceBackList.stream().collect(Collectors.toMap(BackInstanceEntity::getInstanceId, Function.identity()));

        try {
            for (Map.Entry<Long, InstanceEntity> entry : instanceMap.entrySet()) {
                Long instanceId = entry.getKey();
                InstanceEntity instance = entry.getValue();
                String databaseName = instance.getDatabaseName();
                Db db = MysqlUtil.getInstance(instance.getHost(), instance.getPort(), instance.getUsername(), instance.getPassword(), databaseName);
                BackInstanceEntity instanceBack = instanceBackMap.get(instanceId);
                MysqlUtil.runScript(db, FileUtil.getUtf8Reader(String.join(File.separator, sqlDumpDir, instanceBack.getFileName())));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        return Result.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> runScript(Long ticketId) {
        TicketEntity ticket = ticketRepository.getById(ticketId);
        Map<Long, InstanceEntity> instanceMap = this.getInstanceMap(ticketId);
        for (Map.Entry<Long, InstanceEntity> entry : instanceMap.entrySet()) {
            InstanceEntity instance = entry.getValue();
            String databaseName = instance.getDatabaseName();
            Db db = MysqlUtil.getInstance(instance.getHost(), instance.getPort(), instance.getUsername(), instance.getPassword(), databaseName);
            // FIXME: 2023/8/25 事务问题
            MysqlUtil.runSql(db, ticket.getSqlScript());
        }

        ticket.setFollowStatus(TicketFollowStatusEnum.EXECUTED.getCode());
        ticketRepository.updateById(ticket);
        return Result.success(true);
    }

    @Override
    public Result<PagedVO<BackListVO>> backByTicketList(BackListDTO dto) {

        Page<BackEntity> entityPage = backRepository.selectPage(dto);
        List<BackListVO> voList = new ArrayList<>();
        List<BackEntity> records = entityPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(PageUtil.toPage(voList, entityPage));
        }
        Set<Long> userIdSet = records.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
        Map<Long, UserEntity> userMap = userRepository.listByIds(userIdSet).stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        for (BackEntity entity : records) {
            BackListVO vo = BackConvert.toInstanceBackByTicketVO(entity);
            vo.setCreatedByStr(userMap.get(entity.getCreatedBy()).getNickName());
            voList.add(vo);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insertBack(BackAddDTO dto) {
        Long ticketId = dto.getTicketId();
        List<BackEntity> backList = backRepository.listByBizId(ticketId, BackEntity.MODEL_TICKET);
        if (CollUtil.isNotEmpty(backList)) {
            boolean isExist = backList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (isExist) {
                log.warn("数据已存在 dto: {}", dto);
                return Result.error(CodeEnum.DATA_EXIST);
            }
        }
        BackEntity backDO = BackConvert.toDO(dto);
        backDO.setModel(BackEntity.MODEL_TICKET);
        backDO.setBizId(ticketId);
        backRepository.save(backDO);

        Long backId = backDO.getId();

        TicketEntity ticket = ticketRepository.getById(ticketId);
        Map<Long, InstanceEntity> instanceMap = this.getInstanceMap(ticketId);
        String sqlDumpDir = sysConfigManager.findValueByEnum(SysConfigKeyEnum.DATABASE_SQL_DUMP);

        try {
            List<BackInstanceEntity> instanceBackList = new ArrayList<>();
            for (Map.Entry<Long, InstanceEntity> entry : instanceMap.entrySet()) {
                Long instanceId = entry.getKey();
                InstanceEntity instance = entry.getValue();

                String databaseName = instance.getDatabaseName();
                Db db = MysqlUtil.getInstance(instance.getHost(), instance.getPort(), instance.getUsername(), instance.getPassword(), databaseName);
                String fileName = StrUtil.format("{}_{}_{}.sql", ticket.getNumber(), instance.getDatabaseName(), DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMATTER));
                File file = SqlDumper.dumpDatabase(db.getConnection(), new HashSet<>(ListUtil.of(databaseName)), String.join(File.separator, sqlDumpDir, fileName));
                String md5 = SecureUtil.md5(file);

                BackInstanceEntity instanceBack = new BackInstanceEntity();
                instanceBack.setInstanceId(instanceId);
                instanceBack.setFileName(fileName);
                instanceBack.setHashValue(md5);
                instanceBack.setBackId(backId);
                instanceBackList.add(instanceBack);
            }
            backInstanceRepository.saveBatch(instanceBackList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }

        return Result.success(backId);
    }

    @Override
    public Result<BackVO> backDetail(Long backId) {
        BackEntity back = backRepository.getById(backId);
        BackVO vo = BackConvert.toBackVO(back);
        List<BackInstanceEntity> backInstanceList = backInstanceRepository.listByBackId(back.getId());
        if (CollUtil.isNotEmpty(backInstanceList)) {
            String collect = backInstanceList.stream().map(BackInstanceEntity::getFileName).collect(Collectors.joining(", "));
            vo.setFileNameList(collect);
        }
        return Result.success(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> updateBack(BackEditDTO dto) {
        Long id = dto.getId();
        Long ticketId = dto.getTicketId();
        List<BackEntity> backList = backRepository.listByBizId(ticketId, BackEntity.MODEL_TICKET);
        if (CollUtil.isNotEmpty(backList)) {
            boolean isExist = backList.stream().anyMatch(i -> i.getName().equals(dto.getName()) && (!i.getId().equals(id)));
            if (isExist) {
                log.warn("数据已存在 dto: {}", dto);
                return Result.error(CodeEnum.DATA_EXIST);
            }
        }
        BackEntity back = backRepository.getById(id);
        back.setName(dto.getName());
        back.setRemark(dto.getRemark());
        backRepository.updateById(back);
        return Result.success(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> deleteByIdBack(Long id) {
        backRepository.updateToDelete(id);


        String sqlDumpDir = sysConfigManager.findValueByEnum(SysConfigKeyEnum.DATABASE_SQL_DUMP);

        Long userId = UserContextHolder.currentUserId();
        List<BackInstanceEntity> backInstanceList = backInstanceRepository.listByBackId(id);
        if (CollUtil.isNotEmpty(backInstanceList)) {
            for (BackInstanceEntity backInstance : backInstanceList) {
                String oldFileName = backInstance.getFileName();
                String newFileName = StrUtil.format("Removed_{}", oldFileName);
                backInstance.setFileName(newFileName);

                String filePath = String.join(File.separator, sqlDumpDir, oldFileName);
                File file = FileUtil.file(filePath);
                boolean exist = FileUtil.exist(filePath);
                if (exist) {
                    FileUtil.rename(file, newFileName, true);
                }
            }
        }
        backInstanceRepository.updateBatchById(backInstanceList);
        return Result.success(id);
    }

    @SneakyThrows(Exception.class)
    private void batchExecute(List<InstanceEntity> instanceList, String sqlScript){
        for (InstanceEntity instanceDO : instanceList) {
            Db db = MysqlUtil.getInstance(instanceDO.getHost(), instanceDO.getPort(), instanceDO.getUsername(),
                    instanceDO.getPassword(), instanceDO.getDatabaseName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                    sqlScript.getBytes(StandardCharsets.UTF_8))));
            MysqlUtil.runScript(db, reader);
        }
    }

    private TicketEntity beforeCheck(Long id) {
        TicketEntity ticketDO = ticketRepository.getById(id);
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
