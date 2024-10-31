package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.FeedbackAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FeedbackEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FeedbackVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFeedbackRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFeedbackService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISerialNumberService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Feedback ServiceImpl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class FeedbackServiceImpl
        extends BasicServiceImpl<IFeedbackRepository> implements IFeedbackService {

    @Resource
    private IUserService userService;

    @Resource
    private ISerialNumberService serialNumberService;

    @Resource
    private IAuditManager auditManager;

    @Override
    public PagedVO<FeedbackVO> query(FeedbackQueryDTO dto) {
        Page<FeedbackEntity> entityPage = baseRepository.selectPage(dto);
        List<FeedbackVO> voList = new ArrayList<>();
        List<FeedbackEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(FeedbackEntity::getUserId).collect(Collectors.toSet());
            Map<Integer, UserEntity> userEntityMap = userService.getEntityMap(new ArrayList<>(userIdSet));
            for (FeedbackEntity entity : records) {
                FeedbackVO feedbackVO = this.convertToConfigVO(entity);
                UserEntity userEntity = userEntityMap.get(entity.getUserId());
                if (ObjUtil.isNotNull(userEntity)) {
                    feedbackVO.setUserName(userEntity.getActualName());
                }
                voList.add(feedbackVO);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private FeedbackVO convertToConfigVO(FeedbackEntity entity) {
        FeedbackVO feedbackVO = new FeedbackVO();
        feedbackVO.setFeedbackId(entity.getId());
        feedbackVO.setCode(entity.getCode());
        feedbackVO.setFeedbackContent(entity.getFeedbackContent());
        feedbackVO.setFeedbackAttachment(entity.getFeedbackAttachment());
        feedbackVO.setUserId(entity.getUserId());
        feedbackVO.setCreateTime(entity.getCreatedTime());
        return feedbackVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(FeedbackAddDTO dto) {
        FeedbackEntity entity = this.convertToEntity(dto);
        entity.setUserId(UserContextHolder.userId());
        SerialNumberGenerateDTO serialNumberGenerateDTO = new SerialNumberGenerateDTO();
        serialNumberGenerateDTO.setCount(1);
        serialNumberGenerateDTO.setSerialNumberId(1);
        List<String> list = serialNumberService.generate(serialNumberGenerateDTO);
        entity.setCode(list.get(0));
        baseRepository.insert(entity);
        auditManager.saveByAddEnum(entity.getCode(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(FeedbackEntity entity) {
        FeedbackAudit audit = new FeedbackAudit();
        audit.setFeedbackContent(entity.getFeedbackContent());
        audit.setFeedbackAttachment(entity.getFeedbackAttachment());
        audit.setCode(entity.getCode());
        Integer userId = entity.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            UserEntity user = userService.get(userId);
            if (ObjUtil.isNotNull(user)) {
                audit.setUserName(user.getActualName());
            }
        }
        return audit;
    }

    private FeedbackEntity convertToEntity(FeedbackAddDTO dto) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setFeedbackContent(dto.getFeedbackContent());
        feedbackEntity.setFeedbackAttachment(dto.getFeedbackAttachment());
        return feedbackEntity;
    }
}
