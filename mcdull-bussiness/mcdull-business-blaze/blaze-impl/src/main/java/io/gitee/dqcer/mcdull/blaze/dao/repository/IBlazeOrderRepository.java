package io.gitee.dqcer.mcdull.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderQueryDTO;

import java.util.List;


/**
 * 订单合同 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
public interface IBlazeOrderRepository extends IService<BlazeOrderEntity> {


    List<BlazeOrderEntity> queryListByIds(List<Integer> idList);

    List<BlazeOrderEntity> selectList(BlazeOrderQueryDTO dto);

}
