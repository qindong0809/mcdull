package io.gitee.dqcer.mcdull.uac.provider.xcr.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.vo.ProductionScheduleVO;

import java.util.List;


/**
 * 生产进度表 业务接口类
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */

public interface ProductionScheduleService {

    void insert(ProductionScheduleAddDTO dto);

    void update(ProductionScheduleUpdateDTO dto);

    ProductionScheduleVO detail(Integer id);
    void batchDelete(List<Integer> idList);

    void delete(Integer id);


    PagedVO<ProductionScheduleVO> queryPage(ProductionScheduleQueryDTO dto);

    void exportData(ProductionScheduleQueryDTO dto);
}
