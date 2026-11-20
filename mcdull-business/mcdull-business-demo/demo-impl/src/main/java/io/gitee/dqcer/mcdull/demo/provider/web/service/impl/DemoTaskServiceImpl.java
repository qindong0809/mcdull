package io.gitee.dqcer.mcdull.demo.provider.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskCreateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskQueryDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskUpdateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.vo.DemoTaskVO;
import io.gitee.dqcer.mcdull.demo.provider.config.DemoTaskTextResolver;
import io.gitee.dqcer.mcdull.demo.provider.model.bo.DemoTaskBO;
import io.gitee.dqcer.mcdull.demo.provider.model.entity.DemoTaskEntity;
import io.gitee.dqcer.mcdull.demo.provider.web.manager.IDemoTaskManager;
import io.gitee.dqcer.mcdull.demo.provider.web.service.IDemoTaskService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.mysql.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.framework.redis.annotation.CacheExpire;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 演示任务 Service 实现：业务编排、对象转换与缓存边界。
 */
@Service
public class DemoTaskServiceImpl implements IDemoTaskService {

    private static final String CACHE_NAME = "demo:task";

    @Resource
    private IDemoTaskManager demoTaskManager;

    @Resource
    private DemoTaskTextResolver demoTaskTextResolver;

    @Override
    public PagedVO<DemoTaskVO> queryPage(DemoTaskQueryDTO dto) {
        IPage<DemoTaskEntity> page = demoTaskManager.queryPage(dto);
        List<DemoTaskVO> list = page.getRecords().stream()
                .map(this::toBO)
                .map(this::toVO)
                .toList();
        return PageUtil.toPage(list, page);
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "'detail:' + #id", unless = "#result == null")
    @CacheExpire(value = 300, floatRange = 0.1)
    public DemoTaskVO getById(Integer id) {
        DemoTaskEntity entity = demoTaskManager.getById(id);
        if (entity == null) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        return toVO(toBO(entity));
    }

    @Override
    @RedisLock(key = "'demo:task:create:' + #dto.title", timeout = 5)
    @Transactional(rollbackFor = Exception.class)
    public Integer create(DemoTaskCreateDTO dto) {
        DemoTaskEntity entity = new DemoTaskEntity();
        applyCreateFields(dto, entity);
        demoTaskManager.insert(entity);
        return entity.getId();
    }

    @Override
    @RedisLock(key = "'demo:task:update:' + #dto.id", timeout = 5)
    @CacheEvict(cacheNames = CACHE_NAME, key = "'detail:' + #dto.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(DemoTaskUpdateDTO dto) {
        DemoTaskEntity entity = demoTaskManager.getById(dto.getId());
        if (entity == null) {
            LogicCheckUtil.throwDataNotExistException(dto.getId());
        }
        applyCreateFields(dto, entity);
        demoTaskManager.update(entity);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "'detail:' + #id")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        DemoTaskEntity entity = demoTaskManager.getById(id);
        if (entity == null) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        demoTaskManager.deleteById(id);
    }

    private void applyCreateFields(DemoTaskCreateDTO dto, DemoTaskEntity entity) {
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setStatus(dto.getStatus());
        entity.setPriority(dto.getPriority());
        entity.setDueTime(dto.getDueTime());
    }

    private DemoTaskBO toBO(DemoTaskEntity entity) {
        DemoTaskBO bo = new DemoTaskBO();
        bo.setId(entity.getId());
        bo.setTitle(entity.getTitle());
        bo.setContent(entity.getContent());
        bo.setStatus(entity.getStatus());
        bo.setPriority(entity.getPriority());
        bo.setDueTime(entity.getDueTime());
        bo.setCreatedTime(entity.getCreatedTime());
        bo.setUpdatedTime(entity.getUpdatedTime());
        return bo;
    }

    private DemoTaskVO toVO(DemoTaskBO bo) {
        DemoTaskVO vo = new DemoTaskVO();
        vo.setId(bo.getId());
        vo.setTitle(bo.getTitle());
        vo.setContent(bo.getContent());
        vo.setStatus(bo.getStatus());
        vo.setPriority(bo.getPriority());
        vo.setDueTime(bo.getDueTime());
        vo.setCreatedTime(bo.getCreatedTime());
        vo.setUpdatedTime(bo.getUpdatedTime());
        vo.setStatusName(demoTaskTextResolver.statusText(bo.getStatus()));
        vo.setPriorityName(demoTaskTextResolver.priorityText(bo.getPriority()));
        return vo;
    }
}
