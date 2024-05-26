package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FeedbackEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FeedbackMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class FeedbackRepositoryImpl extends ServiceImpl<FeedbackMapper, FeedbackEntity>  implements IFeedbackRepository {

    private static final Logger log = LoggerFactory.getLogger(FeedbackRepositoryImpl.class);

    @Override
    public List<FeedbackEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<FeedbackEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(FeedbackEntity::getId, idList);
        List<FeedbackEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<FeedbackEntity> selectPage(FeedbackQueryDTO param) {
        LambdaQueryWrapper<FeedbackEntity> lambda = new QueryWrapper<FeedbackEntity>().lambda();
        String searchWord = param.getSearchWord();
        if (StrUtil.isNotBlank(searchWord)) {
            lambda.and(i-> i.like(FeedbackEntity::getFeedbackContent, searchWord));
        }
        LocalDate startDate = param.getStartDate();
        LocalDate endDate = param.getEndDate();
        if (ObjUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(RelEntity::getCreatedTime, startDate,
                    LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public FeedbackEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer insert(FeedbackEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }


    @Override
    public boolean exist(FeedbackEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }


    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}