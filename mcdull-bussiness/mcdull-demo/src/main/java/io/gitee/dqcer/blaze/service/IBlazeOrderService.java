package io.gitee.dqcer.blaze.service;

import io.gitee.dqcer.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderSearchDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 订单合同 业务接口类
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

public interface IBlazeOrderService {

    void insert(BlazeOrderAddDTO dto, List<MultipartFile> fileList);

    void update(BlazeOrderUpdateDTO dto, List<MultipartFile> fileList);

    BlazeOrderVO detail(Integer id);

    void delete(Integer id);


    PagedVO<BlazeOrderVO> queryPage(BlazeOrderQueryDTO dto);

    boolean existByTalentCertificateId(List<Integer> talentCertificateIdList);

    boolean existByCertificateRequirementsIdList(List<Integer> certificateRequirementsIdList);

    void exportData(BlazeOrderQueryDTO dto);

    Map<Integer, Boolean> getMapByTalentCertId(Set<Integer> collect);

    Map<Integer, Boolean> getMapByCustomerCertId(Collection<Integer> customerCertList);

    List<BlazeOrderEntity> list();

    List<LabelValueVO<Integer, String>> getCustomerCertListByOrderId(Integer orderId);

    List<LabelValueVO<Integer, String>> getTalentCertListByOrderId(Integer customerCertId, Integer orderId);

    void approve(ApproveDTO dto);

    List<LabelValueVO<Integer, String>> getTalentCertList(BlazeOrderSearchDTO searchDTO);

    List<LabelValueVO<Integer, String>> getCustomCertList(BlazeOrderSearchDTO pkDTO);

    BlazeOrderEntity getByCustomerCertId(Integer customerCertId);

    BlazeOrderEntity getByTalentCertId(Integer talentCertId);
}
