package io.gitee.dqcer.mcdull.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailQueryDTO;

import java.util.List;


/**
 * @author dqcer
` * @since 2025-01-18 11:33:31
` */
public interface IBlazeOrderDetailRepository extends IService<BlazeOrderDetailEntity> {


    Page<BlazeOrderDetailEntity> selectPage(BlazeOrderDetailQueryDTO dto);

    List<BlazeOrderDetailEntity> getByOrderId(List<Integer> orderIdList);
}
