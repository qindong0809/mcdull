package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
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
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class HelpDocRepositoryImpl
        extends ServiceImpl<HelpDocMapper, HelpDocEntity>  implements IHelpDocRepository {

    private static final Logger log = LoggerFactory.getLogger(HelpDocRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< ConfigEntity >}
     */
    @Override
    public List<HelpDocEntity> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<HelpDocEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(HelpDocEntity::getId, idList);
        List<HelpDocEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page< ConfigEntity >}
     */
    @Override
    public Page<HelpDocEntity> selectPage(HelpDocQueryDTO param) {
        LambdaQueryWrapper<HelpDocEntity> lambda = new QueryWrapper<HelpDocEntity>().lambda();
        Long helpDocCatalogId = param.getHelpDocCatalogId();
        if (ObjUtil.isNotNull(helpDocCatalogId)) {
            lambda.eq(HelpDocEntity::getHelpDocCatalogId, helpDocCatalogId);
        }
        String keywords = param.getKeywords();
        if (StrUtil.isNotBlank(keywords)) {
            lambda.like(HelpDocEntity::getTitle, keywords);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    @Override
    public HelpDocEntity getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(HelpDocEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(HelpDocEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<HelpDocEntity> selectList(Long userId) {
        LambdaQueryWrapper<HelpDocEntity> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }

    @Override
    public List<HelpDocEntity> listByCatalogId(Long helpDocCatalogId) {
        LambdaQueryWrapper<HelpDocEntity> query = Wrappers.lambdaQuery();
        query.eq(HelpDocEntity::getHelpDocCatalogId, helpDocCatalogId);
        return baseMapper.selectList(query);
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}