package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FeedbackAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FeedbackEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FeedbackVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.FeedbackMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFeedbackService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        extends BasicCurdServiceImpl<FeedbackMapper, FeedbackEntity> implements IFeedbackService {

    @Resource
    private IUserManager userManager;

    @Resource
    private ISerialNumberService serialNumberService;

    @Override
    public PagedVO<FeedbackVO> query(FeedbackQueryDTO dto) {
        Page<FeedbackEntity> entityPage = this.selectPage(dto);
        List<FeedbackVO> voList = new ArrayList<>();
        List<FeedbackEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(FeedbackEntity::getUserId).collect(Collectors.toSet());
            Map<Integer, UserEntity> userEntityMap = userManager.getEntityMap(new ArrayList<>(userIdSet));
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
        baseMapper.insert(entity);
    }


    private FeedbackEntity convertToEntity(FeedbackAddDTO dto) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setFeedbackContent(dto.getFeedbackContent());
        feedbackEntity.setFeedbackAttachment(dto.getFeedbackAttachment());
        return feedbackEntity;
    }

    public List<FeedbackEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<FeedbackEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(FeedbackEntity::getId, idList);
        return baseMapper.selectList(wrapper);
    }

    public Page<FeedbackEntity> selectPage(FeedbackQueryDTO param) {
        LambdaQueryWrapper<FeedbackEntity> lambda = new QueryWrapper<FeedbackEntity>().lambda();
        String searchWord = param.getSearchWord();
        if (CharSequenceUtil.isNotBlank(searchWord)) {
            lambda.and(i-> i.like(FeedbackEntity::getFeedbackContent, searchWord));
        }
        LocalDate startDate = param.getStartDate();
        LocalDate endDate = param.getEndDate();
        if (ObjectUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(RelEntity::getCreatedTime, startDate,
                LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public FeedbackEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    public Integer insert(FeedbackEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public boolean exist(FeedbackEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }

}
