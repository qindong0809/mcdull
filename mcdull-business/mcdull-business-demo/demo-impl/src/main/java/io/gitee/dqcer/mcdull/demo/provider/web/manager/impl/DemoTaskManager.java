package io.gitee.dqcer.mcdull.demo.provider.web.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskQueryDTO;
import io.gitee.dqcer.mcdull.demo.provider.model.entity.DemoTaskEntity;
import io.gitee.dqcer.mcdull.demo.provider.web.mapper.DemoTaskMapper;
import io.gitee.dqcer.mcdull.demo.provider.web.manager.IDemoTaskManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 演示任务 Manager 实现：只负责数据访问与查询条件组装。
 */
@Component
public class DemoTaskManager implements IDemoTaskManager {

    @Resource
    private DemoTaskMapper demoTaskMapper;

    @Override
    public IPage<DemoTaskEntity> queryPage(DemoTaskQueryDTO dto) {
        LambdaQueryWrapper<DemoTaskEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(dto.getTitle()), DemoTaskEntity::getTitle, dto.getTitle())
                .like(StrUtil.isNotBlank(dto.getKeyword()), DemoTaskEntity::getTitle, dto.getKeyword())
                .eq(dto.getStatus() != null, DemoTaskEntity::getStatus, dto.getStatus())
                .orderByDesc(DemoTaskEntity::getCreatedTime);
        return demoTaskMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), wrapper);
    }

    @Override
    public DemoTaskEntity getById(Integer id) {
        return demoTaskMapper.selectById(id);
    }

    @Override
    public void insert(DemoTaskEntity entity) {
        demoTaskMapper.insert(entity);
    }

    @Override
    public void update(DemoTaskEntity entity) {
        demoTaskMapper.updateById(entity);
    }

    @Override
    public void deleteById(Integer id) {
        demoTaskMapper.deleteById(id);
    }
}
