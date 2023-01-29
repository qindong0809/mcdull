package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.NoticeMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.INoticeRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
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
public class NoticeRepositoryImpl extends ServiceImpl<NoticeMapper, NoticeDO>  implements INoticeRepository {

    private static final Logger log = LoggerFactory.getLogger(NoticeRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List<NoticeDO>}
     */
    @Override
    public List<NoticeDO> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<NoticeDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(NoticeDO::getId, idList);
        wrapper.eq(NoticeDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<NoticeDO> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page<NoticeDO>}
     */
    @Override
    public Page<NoticeDO> selectPage(NoticeLiteDTO param) {
        LambdaQueryWrapper<NoticeDO> lambda = new QueryWrapper<NoticeDO>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        return baseMapper.selectPage(new Page<>(param.getCurrentPage(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link NoticeDO}
     */
    @Override
    public NoticeDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(NoticeDO entity) {
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

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(NoticeDO entity) {
        List<NoticeDO> list = baseMapper.selectList(Wrappers.lambdaQuery(entity));
        return list.isEmpty();
    }
}