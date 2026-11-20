package io.gitee.dqcer.mcdull.demo.provider.web.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskQueryDTO;
import io.gitee.dqcer.mcdull.demo.provider.model.entity.DemoTaskEntity;

/**
 * 演示任务数据访问编排。
 */
public interface IDemoTaskManager {

    /**
     * 分页查询。
     *
     * @param dto 查询条件
     * @return 分页实体
     */
    IPage<DemoTaskEntity> queryPage(DemoTaskQueryDTO dto);

    /**
     * 按主键查询。
     *
     * @param id 主键
     * @return 实体，不存在返回 null
     */
    DemoTaskEntity getById(Integer id);

    /**
     * 新增。
     *
     * @param entity 实体
     */
    void insert(DemoTaskEntity entity);

    /**
     * 修改。
     *
     * @param entity 实体
     */
    void update(DemoTaskEntity entity);

    /**
     * 删除。
     *
     * @param id 主键
     */
    void deleteById(Integer id);
}
