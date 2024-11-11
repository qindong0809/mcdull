package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.BizAuditVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IBizAuditRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IMenuManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IBizAuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId,
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
        baseRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<BizAuditVO> queryPage(BizAuditQueryDTO queryForm) {
        List<BizAuditVO> voList = new ArrayList<>();
        Page<BizAuditEntity> entityPage = baseRepository.selectPage(queryForm);
        List<BizAuditEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<String> codeSet = recordList.stream().map(BizAuditEntity::getBizTypeCode).collect(Collectors.toSet());
            Map<String, MenuEntity> nameMap = menuManager.getMenuName(new ArrayList<>(codeSet));
            List<MenuEntity> listAll = menuManager.listAll();
            List<String> loginList = recordList.stream().map(BizAuditEntity::getOperator).collect(Collectors.toList());
            Map<String, String> nameMapByLoginName = userManager.getNameMapByLoginName(loginList);
            for (BizAuditEntity entity : recordList) {
                BizAuditVO vo = this.convertToVO(entity);
                MenuEntity menuEntity = nameMap.get(entity.getBizTypeCode());
                if (menuEntity != null) {
                    vo.setBizTypeName(menuEntity.getMenuName());
                    List<String> rootNameList = this.getRootName(listAll, menuEntity.getId());
                    Collections.reverse(rootNameList);
                    vo.setBizTypeRootName(StrUtil.join(StrUtil.SLASH, rootNameList));
                }
                vo.setOperationName(IEnum.getTextByCode(OperationTypeEnum.class, entity.getOperation()));
                vo.setOperatorName(nameMapByLoginName.get(entity.getOperator()));
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exportData(BizAuditQueryDTO dto) {
        PageUtil.setMaxPageSize(dto);
        PagedVO<BizAuditVO> page = this.queryPage(dto);
        List<BizAuditVO> list = new ArrayList<>();
        if (ObjUtil.isNotNull(page)) {
            list = page.getList();
        }
        Map<String, String> titleMap = new HashMap<>(8);
        titleMap.put("业务类型", "bizTypeName");
        titleMap.put("业务类型路径", "bizTypeRootName");
        titleMap.put("业务索引", "bizIndex");
        titleMap.put("业务动作", "operationName");
        titleMap.put("业务内容", "comment");
        titleMap.put("操作人", "operator");
        titleMap.put("操作时间", "operationTime");
        List<Map<String, String>> mapList = new ArrayList<>();
        for (BizAuditVO vo : list) {
            Map<String, String> map = new HashMap<>(8);
            map.put("bizTypeName", vo.getBizTypeName());
            map.put("bizTypeRootName", vo.getBizTypeRootName());
            map.put("bizIndex", vo.getBizIndex());
            map.put("operationName", vo.getOperationName());
            map.put("comment", vo.getComment());
            map.put("operator", vo.getOperator());
            map.put("operationTime", vo.getOperationTime().toString());
            mapList.add(map);
        }
        commonManager.exportExcel("业务操作记录", StrUtil.EMPTY, titleMap, mapList);
    }

    public List<String> getRootName(List<MenuEntity> list, Integer id) {
        if (ObjUtil.isNotNull(id)) {
            MenuEntity menuEntity = list.stream().filter(item -> ObjUtil.equal(item.getId(), id)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(menuEntity)) {
                List<String> arrayList = new ArrayList<>();
                arrayList.add(menuEntity.getMenuName());
                List<String> l = this.getRootName(list, menuEntity.getParentId());
                if (CollUtil.isNotEmpty(l)) {
                    arrayList.addAll(l);
                }
                return arrayList;
            }
        }
        return null;
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
