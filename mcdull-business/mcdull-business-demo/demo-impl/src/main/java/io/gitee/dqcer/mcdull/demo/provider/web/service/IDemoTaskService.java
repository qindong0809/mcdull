package io.gitee.dqcer.mcdull.demo.provider.web.service;

import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskCreateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskQueryDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskUpdateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.vo.DemoTaskVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

/**
 * 演示任务业务接口。
 */
public interface IDemoTaskService {

    /**
     * 分页查询。
     *
     * @param dto 查询条件
     * @return 分页视图
     */
    PagedVO<DemoTaskVO> queryPage(DemoTaskQueryDTO dto);

    /**
     * 详情查询。
     *
     * @param id 主键
     * @return 视图对象
     */
    DemoTaskVO getById(Integer id);

    /**
     * 新建任务。
     *
     * @param dto 入参
     * @return 新任务 ID
     */
    Integer create(DemoTaskCreateDTO dto);

    /**
     * 修改任务。
     *
     * @param dto 入参
     */
    void update(DemoTaskUpdateDTO dto);

    /**
     * 删除任务。
     *
     * @param id 主键
     */
    void delete(Integer id);
}
