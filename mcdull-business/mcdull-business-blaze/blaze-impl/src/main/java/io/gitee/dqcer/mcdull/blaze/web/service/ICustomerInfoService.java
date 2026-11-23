package io.gitee.dqcer.mcdull.blaze.web.service;

import io.gitee.dqcer.mcdull.blaze.model.dto.CustomerInfoAddDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.CustomerInfoQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.CustomerInfoUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.model.vo.CustomerInfoVO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;


/**
 * 企业信息 业务接口类
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

public interface ICustomerInfoService {

    void insert(CustomerInfoAddDTO dto);

    void update(CustomerInfoUpdateDTO dto);

    CustomerInfoVO detail(Integer id);

    void batchDelete(List<Integer> idList);

    PagedVO<CustomerInfoVO> queryPage(CustomerInfoQueryDTO dto);

    List<LabelValueVO<Integer, String>> list();

    boolean exportData(CustomerInfoQueryDTO dto);
}
