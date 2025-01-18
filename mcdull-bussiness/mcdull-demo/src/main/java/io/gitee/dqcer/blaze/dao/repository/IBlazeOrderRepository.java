package io.gitee.dqcer.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;

import java.util.List;


/**
 * 订单合同 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
public interface IBlazeOrderRepository extends IService<BlazeOrderEntity> {


    List<BlazeOrderEntity> queryListByIds(List<Integer> idList);

    Page<BlazeOrderEntity> selectPage(BlazeOrderQueryDTO dto);

}
