package io.gitee.dqcer.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailQueryDTO;


/**
 * @author dqcer
` * @since 2025-01-18 11:33:31
` */
public interface IBlazeOrderDetailRepository extends IService<BlazeOrderDetailEntity> {


    Page<BlazeOrderDetailEntity> selectPage(BlazeOrderDetailQueryDTO dto);
}
