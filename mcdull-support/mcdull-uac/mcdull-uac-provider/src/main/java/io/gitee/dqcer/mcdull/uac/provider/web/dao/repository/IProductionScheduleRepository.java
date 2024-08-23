package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.entity.ProductionScheduleEntity;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleQueryDTO;

import java.util.List;


/**
 * 生产进度表 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */
public interface IProductionScheduleRepository extends IService<ProductionScheduleEntity> {


    List<ProductionScheduleEntity> queryListByIds(List<Integer> idList);

    Page<ProductionScheduleEntity> selectPage(ProductionScheduleQueryDTO dto);

}
