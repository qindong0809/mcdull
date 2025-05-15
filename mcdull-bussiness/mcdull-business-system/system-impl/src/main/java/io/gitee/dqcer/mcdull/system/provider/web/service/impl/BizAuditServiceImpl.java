package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.AuditUtil;
import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.BizAuditVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IBizAuditFieldRepository;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IBizAuditRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IMenuManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IBizAuditService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Biz Audit Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class BizAuditServiceImpl
        extends BasicServiceImpl<IBizAuditRepository> implements IBizAuditService {

    @Resource
    private IMenuManager menuManager;
    @Resource
    private IUserManager userManager;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IBizAuditFieldRepository bizAuditFieldRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId,
                       String comment, String operator, Date operationTime, String ext) {
        BizAuditEntity entity = new BizAuditEntity();
        entity.setBizTypeCode(bizTypeCode);
        entity.setComment(comment);
        entity.setOperationTime(operationTime);
        entity.setOperator(operator);
        entity.setOperation(operationTypeEnum.getCode());
        entity.setBizId(bizId);
        entity.setBizIndex(bizIndex);
        entity.setExt(ext);
        entity.setTraceId(UserContextHolder.getSession().getTraceId());
        baseRepository.save(entity);
        return entity.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<BizAuditVO> queryPage(BizAuditQueryDTO queryForm) {
        List<BizAuditVO> voList = new ArrayList<>();
        Page<BizAuditEntity> entityPage = baseRepository.selectPage(queryForm);
        List<BizAuditEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            List<String> loginList = recordList.stream().map(BizAuditEntity::getOperator).collect(Collectors.toList());
            Map<String, String> nameMapByLoginName = userManager.getNameMapByLoginName(loginList);
            List<LabelValueVO<String, String>> nameCodeList = menuManager.getNameCodeList();
            Map<String, String> codeMap = nameCodeList.stream().collect(Collectors.toMap(LabelValueVO::getValue, LabelValueVO::getLabel));
            List<Integer> list = recordList.stream().map(BizAuditEntity::getId).collect(Collectors.toList());
            Map<Integer, List<BizAuditFieldEntity>> map = bizAuditFieldRepository.map(list);
            for (BizAuditEntity entity : recordList) {
                BizAuditVO vo = this.convertToVO(entity);
                vo.setBizTypeName(codeMap.get(entity.getBizTypeCode()));
                vo.setOperationName(IEnum.getTextByCode(OperationTypeEnum.class, entity.getOperation()));
                vo.setOperatorName(nameMapByLoginName.get(entity.getOperator()));
                if (OperationTypeEnum.ADD.getCode().equals(entity.getOperation())
                        || OperationTypeEnum.UPDATE.getCode().equals(entity.getOperation())) {
                    List<BizAuditFieldEntity> auditFieldList = map.get(vo.getId());
                    if (CollUtil.isNotEmpty(auditFieldList)) {
                        List<AuditUtil.FieldDiff> diffList = this.convertFieldDiff(auditFieldList);
                        String comment = AuditUtil.compareStr(diffList, OperationTypeEnum.UPDATE.getCode().equals(entity.getOperation()));
                        vo.setComment(comment);
                        // 排序
                        diffList.sort(Comparator.comparingInt(AuditUtil.FieldDiff::getSortOrder));
                        vo.setDiffList(diffList);
                    }
                }
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private List<AuditUtil.FieldDiff> convertFieldDiff(List<BizAuditFieldEntity> auditFieldList) {
        List<AuditUtil.FieldDiff> diffList = new ArrayList<>();
        for (BizAuditFieldEntity auditField : auditFieldList) {
            AuditUtil.FieldDiff diff = new AuditUtil.FieldDiff();
            diff.setBeforeValue(auditField.getOldValue());
            diff.setAfterValue(auditField.getNewValue());
            diff.setFieldName(auditField.getFieldName());
            diff.setSortOrder(auditField.getSortOrder());
            diffList.add(diff);
        }
        return diffList;
    }

    @Override
    public boolean exportData(BizAuditQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<BizAuditVO, ?>>> getTitleList() {
        return ListUtil.of(
                Pair.of("业务类型", BizAuditVO::getBizTypeName),
                Pair.of("业务类型路径", BizAuditVO::getBizTypeRootName),
                Pair.of("业务索引", BizAuditVO::getBizIndex),
                Pair.of("业务动作", BizAuditVO::getOperationName),
                Pair.of("业务内容", BizAuditVO::getComment),
                Pair.of("操作人", BizAuditVO::getOperator),
                Pair.of("操作时间", BizAuditVO::getOperationTime));
    }

    private BizAuditVO convertToVO(BizAuditEntity entity) {
        BizAuditVO vo = new BizAuditVO();
        vo.setId(entity.getId());
        vo.setBizTypeCode(entity.getBizTypeCode());
        vo.setBizIndex(entity.getBizIndex());
        vo.setOperation(entity.getOperation());
        vo.setComment(entity.getComment());
        vo.setOperator(entity.getOperator());
        vo.setOperationTime(entity.getOperationTime());
        return vo;
    }
}
