package io.gitee.dqcer.mcdull.focus.web.service;

import java.util.Map;

/**
 * 统计视图服务
 *
 * @author dqcer
 */
public interface IStatsService {

    /**
     * 统计概览
     *
     * @param userId 用户 ID
     * @param period 统计周期，week 之外按自然月统计
     * @return 概览数据
     */
    Map<String, Object> overview(Integer userId, String period);

    /**
     * 年度坚持热力图
     *
     * @param userId 用户 ID
     * @param year   年份
     * @return 按天聚合的活跃数据及摘要
     */
    Map<String, Object> heatmap(Integer userId, Integer year);

    /**
     * 周报数据
     *
     * @param userId 用户 ID
     * @return 周报数据
     */
    Map<String, Object> weeklyReport(Integer userId);
}
