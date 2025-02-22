package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailSendHistoryQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.EmailSendHistoryEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.EmailTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EmailSendHistoryVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IEmailSendHistoryRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailSendHistoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Email send history service impl
 *
 * @author dqcer
 * @since 2024/03/25
 */
@Service
public class EmailSendHistoryServiceImpl
        extends BasicServiceImpl<IEmailSendHistoryRepository> implements IEmailSendHistoryService {

    @Resource
    private ICommonManager commonManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(EmailTypeEnum typeEnum, List<String> sendToList, List<String> ccList, String title, String content) {
        if (CollUtil.isEmpty(sendToList) || StrUtil.isBlank(title) || StrUtil.isBlank(content)) {
            LogHelp.error(log, "insert email send history fail, sendToList: {}, title: {}, content: {}", sendToList, title, content);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        EmailSendHistoryEntity entity = new EmailSendHistoryEntity();
        entity.setType(typeEnum.getCode());
        entity.setSentTo(String.join(StrUtil.COMMA, sendToList));
        if (CollUtil.isNotEmpty(ccList)) {
            entity.setCc(String.join(StrUtil.COMMA, ccList));
        }
        entity.setTitle(title);
        entity.setContent(content);
        baseRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(EmailTypeEnum typeEnum, String sendTo, String title, String content) {
        this.insert(typeEnum, ListUtil.of(sendTo), null, title, content);
    }

    @Override
    public PagedVO<EmailSendHistoryVO> queryPage(EmailSendHistoryQueryDTO queryDTO) {
        List<EmailSendHistoryVO> list = new ArrayList<>();
        Page<EmailSendHistoryEntity> entityPage = baseRepository.selectPage(queryDTO);
        if (ObjUtil.isNotNull(entityPage)) {
            List<EmailSendHistoryEntity> records = entityPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (EmailSendHistoryEntity record : records) {
                    EmailSendHistoryVO vo = new EmailSendHistoryVO();
                    vo.setId(record.getId());
                    vo.setType(record.getType());
                    vo.setTypeName(IEnum.getTextByCode(EmailTypeEnum.class, record.getType()));
                    vo.setSentTo(record.getSentTo());
                    vo.setCc(record.getCc());
                    vo.setTitle(record.getTitle());
                    vo.setContent(record.getContent());
                    vo.setFileIdArray(record.getFileIdArray());
                    vo.setCreateTime(record.getCreatedTime());
                    list.add(vo);
                }
            }
        }
         return PageUtil.toPage(list, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exportData(EmailSendHistoryQueryDTO dto) {
        commonManager.exportExcel(new EmailSendHistoryQueryDTO(), this::queryPage, "业务操作记录", this.getTitleMap(), this::convertMap);
    }

    private Map<String, String> getTitleMap() {
        Map<String, String> titleMap = new HashMap<>(8);
       titleMap.put("类型名称", "typeName");
        titleMap.put("接收人", "sentTo");
        titleMap.put("抄送人", "cc");
        titleMap.put("标题", "title");
        titleMap.put("内容", "content");
        titleMap.put("发送时间", "createTime");
        return titleMap;
    }

    private Map<String, String> convertMap(EmailSendHistoryVO vo) {
        Map<String, String> map = new HashMap<>(8);
        map.put("typeName", vo.getTypeName());
        map.put("sentTo", vo.getSentTo());
        map.put("cc", vo.getCc());
        map.put("title", vo.getTitle());
        map.put("content", vo.getContent());
        map.put("createTime", commonManager.convertDateByUserTimezone(vo.getCreateTime()));
        return map;
    }
}
