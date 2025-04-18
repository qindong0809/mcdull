package io.gitee.dqcer.mcdull.blaze.service;

import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

public interface IBlazeOrderDetailService {

    PagedVO<BlazeOrderDetailVO> queryPage(BlazeOrderDetailQueryDTO dto);

    void exportData(BlazeOrderDetailQueryDTO dto);

    void insert(BlazeOrderDetailAddDTO dto, List<MultipartFile> fileList);

    void update(BlazeOrderDetailUpdateDTO dto, List<MultipartFile> fileList);

    void delete(Integer id);

    List<BlazeOrderDetailListVO> getOderListByTalent();

    List<BlazeOrderDetailListVO> getOderListByCustomer();

    boolean isTalent(Integer id);

    List<LabelValueVO<Integer, String>> getResponsibleList();

    BlazeOrderDetailEntity getByOrderId(Integer id);

    List<BlazeOrderDetailEntity> getByOrderId(List<Integer> orderIdList);

    void approve(ApproveDTO dto);
}
