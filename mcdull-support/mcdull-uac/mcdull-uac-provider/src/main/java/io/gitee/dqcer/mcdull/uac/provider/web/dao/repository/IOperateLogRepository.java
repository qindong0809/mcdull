package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;

import java.util.List;
import java.util.Map;

/**
 * Operate log repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IOperateLogRepository extends IRepository<OperateLogEntity> {

    /**
     * 分页
     *
     * @param param      param
     * @param userIdList userIdList
     * @return {@link Page}<{@link OperateLogEntity}>
     */
    Page<OperateLogEntity> selectPage(OperateLogQueryDTO param, List<Integer> userIdList);

    /**
     * 首页
     *
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    List<Map<String, Object>> home();

    /**
     * 仅获取模块
     *
     * @return {@link List }<{@link OperateLogEntity }>
     */
    List<OperateLogEntity> getOnlyModule();
}