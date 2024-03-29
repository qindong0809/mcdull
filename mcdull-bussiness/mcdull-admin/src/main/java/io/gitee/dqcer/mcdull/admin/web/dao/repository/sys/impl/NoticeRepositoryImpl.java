package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.NoticeMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.INoticeRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 通知公告表 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-19
*/
@Service
public class NoticeRepositoryImpl extends ServiceImpl<NoticeMapper, NoticeEntity>  implements INoticeRepository {

    private static final Logger log = LoggerFactory.getLogger(NoticeRepositoryImpl.class);

    @Override
    public boolean checkBusinessUnique(NoticeEntity entity) {
        LambdaQueryWrapper<NoticeEntity> query = Wrappers.lambdaQuery();
        Long entityId = entity.getId();
        if (cn.hutool.core.util.ObjUtil.isNotNull(entityId)) {
            query.ne(IdEntity::getId, entityId);
        }
        query.eq(NoticeEntity::getNoticeTitle, entity.getNoticeTitle());
        return !baseMapper.exists(query);
    }

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< NoticeEntity >}
     */
    @Override
    public List<NoticeEntity> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<NoticeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(NoticeEntity::getId, idList);
        List<NoticeEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<NoticeEntity> selectPage(NoticeLiteDTO dto) {
        LambdaQueryWrapper<NoticeEntity> lambda = new QueryWrapper<NoticeEntity>().lambda();
        String noticeTitle = dto.getNoticeTitle();
        if (StrUtil.isNotBlank(noticeTitle)) {
            lambda.like(NoticeEntity::getNoticeTitle, noticeTitle);
        }
        String noticeType = dto.getNoticeType();
        if (StrUtil.isNotBlank(noticeType)) {
            lambda.eq(NoticeEntity::getNoticeType, noticeType);
        }
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link NoticeEntity}
     */
    @Override
    public NoticeEntity getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(NoticeEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    @Override
    public void deleteById(Long id) {
        int rowSize = baseMapper.deleteById(id);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, id:{}", rowSize, id);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 批量删除
     *
     * @param ids 主键集
     * @return int 受影响的行数
     */
    @Override
    public int deleteBatchIds(List<Long> ids) {
         return baseMapper.deleteBatchIds(ids);
    }

}