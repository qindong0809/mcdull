package io.gitee.dqcer.blaze.service;

import io.gitee.dqcer.blaze.domain.form.BlazeOrderAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;


/**
 * 订单合同 业务接口类
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

public interface IBlazeOrderService {

    void insert(BlazeOrderAddDTO dto);

    void update(BlazeOrderUpdateDTO dto);

    BlazeOrderVO detail(Integer id);

    void delete(Integer id);


    PagedVO<BlazeOrderVO> queryPage(BlazeOrderQueryDTO dto);

    boolean existByTalentCertificateId(List<Integer> talentCertificateIdList);

    boolean existByCertificateRequirementsIdList(List<Integer> certificateRequirementsIdList);

    void exportData(BlazeOrderQueryDTO dto);
}
