package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.NoticeConvert;
import io.gitee.dqcer.mcdull.admin.web.service.sys.INoticeService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.INoticeRepository;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
* 通知公告表 业务实现类
*
* @author dqcer
* @since 2023-01-19
*/
@Service
public class NoticeServiceImpl implements INoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Resource
    private INoticeRepository noticeRepository;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> insert(NoticeLiteDTO dto) {

        NoticeDO tempEntity = new NoticeDO();
        // TODO: 业务唯一性效验, 除非业务场景不需要
        boolean exist = noticeRepository.exist(tempEntity);
        if (exist) {
            log.warn("数据已存在 query:{}", tempEntity);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        NoticeDO entity = NoticeConvert.convertToNoticeDO(dto);
        Long entityId = noticeRepository.insert(entity);
        return Result.ok(entityId);
    }

    /**
     * 详情
     *
     * @param id 主键
     * @return {@link Result<NoticeVO> }
     */
    @Transactional(readOnly = true)
    @Override
    public Result<NoticeVO> detail(Long id) {
        NoticeDO entity = noticeRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(NoticeConvert.convertToNoticeVO(entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(NoticeLiteDTO dto) {
        Long id = dto.getId();

        NoticeDO dbData = noticeRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        NoticeDO entity = NoticeConvert.convertToNoticeDO(dto);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = noticeRepository.updateById(entity);
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

        NoticeDO dbData = noticeRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        NoticeDO entity = new NoticeDO();
        entity.setId(id);
//        entity.setStatus(dto.getStatus());
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        boolean success = noticeRepository.updateById(entity);

        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }

    /**
     * 根据主键删除
     *
     * @param dto 参数
     * @return {@link Result<Long>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> delete(NoticeLiteDTO dto) {
        Long id = dto.getId();
        noticeRepository.deleteById(dto.getId());
        return Result.ok(id);
    }

    /**
     * 根据主键集删除
     *
     * @param ids id集
     * @return {@link Result<List>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<Long>> deleteByIds(List<Long> ids) {
        noticeRepository.deleteBatchIds(ids);
        return Result.ok(ids);
    }

    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<List<NoticeVO>> queryByIds(List<Long> ids) {
         List<NoticeDO> listEntity = noticeRepository.queryListByIds(ids);
         List<NoticeVO> voList = new ArrayList<>();
         for (NoticeDO entity : listEntity) {
            voList.add(NoticeConvert.convertToNoticeVO(entity));
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
    public Result<PagedVO<NoticeVO>> listByPage(NoticeLiteDTO dto) {
        Page<NoticeDO> entityPage = noticeRepository.selectPage(dto);
        List<NoticeVO> voList = new ArrayList<>();
        for (NoticeDO entity : entityPage.getRecords()) {
            voList.add(NoticeConvert.convertToNoticeVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
