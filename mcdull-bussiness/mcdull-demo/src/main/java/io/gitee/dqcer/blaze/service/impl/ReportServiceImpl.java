package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import io.gitee.dqcer.blaze.domain.enums.ApproveEnum;
import io.gitee.dqcer.blaze.domain.form.*;
import io.gitee.dqcer.blaze.domain.vo.*;
import io.gitee.dqcer.blaze.service.*;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements IReportService {

    @Resource
    private ICertificateRequirementsService certificateRequirementsService;
    @Resource
    private ITalentCertificateService talentCertificateService;
    @Resource
    private IBlazeOrderService orderService;;
    @Resource
    private ITalentService talentService;
    @Resource
    private ICustomerInfoService customerInfoService;

    @Override
    public ReportViewVO getReportView() {
        Map<String, Long> customerMap = new HashMap<>();
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        List<CertificateRequirementsVO> list = PageUtil.getAllList(dto, d -> certificateRequirementsService.queryPage(d));
        if (CollUtil.isNotEmpty(list)) {
            // 根据certificateLevelName + specialtyName 分组
            customerMap = list.stream().collect(Collectors
                    .groupingBy(item -> item.getCertificateLevelName() + "\n" + item.getSpecialtyName(), Collectors.counting()));
        }
        Map<String, Long> talentMap = new HashMap<>();
        TalentCertificateQueryDTO talentCertificateQueryDTO = new TalentCertificateQueryDTO();
        talentCertificateQueryDTO.setApprove(ApproveEnum.APPROVE.getCode());
        List<TalentCertificateVO> talentList = PageUtil.getAllList(talentCertificateQueryDTO, d -> talentCertificateService.queryPage(d));
        if (CollUtil.isNotEmpty(talentList)) {
            // 根据certificateLevelName + specialtyName 分组
            talentMap = talentList.stream().collect(Collectors
                    .groupingBy(item -> item.getCertificateLevelName() + "\n" + item.getSpecialtyName(), Collectors.counting()));
        }
        List<String> typeName = new ArrayList<>();
        if (MapUtil.isNotEmpty(customerMap)) {
            typeName.addAll(customerMap.keySet());
        }
        if (MapUtil.isNotEmpty(talentMap)) {
            Set<String> strings = talentMap.keySet();
            for (String s : strings) {
                if (!typeName.contains(s)) {
                    typeName.add(s);
                }
            }
        }
        if (CollUtil.isNotEmpty(typeName)) {
            typeName.sort(Comparator.comparing(String::toString));
        }

        List<Integer> customerValue = new ArrayList<>();
        for (String s : typeName) {
            if (customerMap.containsKey(s)) {
                customerValue.add(Math.toIntExact(customerMap.get(s)));
            } else {
                customerValue.add(0);
            }
        }
        List<Integer> talentValue = new ArrayList<>();
        for (String s : typeName) {
            if (talentMap.containsKey(s)) {
                talentValue.add(Math.toIntExact(talentMap.get(s)));
            } else {
                talentValue.add(0);
            }
        }
        ReportViewVO reportViewVO = new ReportViewVO();
        reportViewVO.setTypeName(typeName);
        reportViewVO.setCustomerValue(customerValue);
        reportViewVO.setTalentValue(talentValue);
        return reportViewVO;
    }

    @Override
    public Integer getMatchSuccessCountTotal() {
        BlazeOrderQueryDTO dto = new BlazeOrderQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        List<BlazeOrderVO> list = PageUtil.getAllList(dto, d -> orderService.queryPage(d));
        return list.size();
    }


    @Override
    public Integer getEnterpriseCertificateDemandPendingMatchCount() {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        List<CertificateRequirementsVO> list = PageUtil.getAllList(dto, d -> certificateRequirementsService.queryPage(d));
        return list.size();
    }

    @Override
    public Integer getTalentCertificatePendingMatchCount() {
        TalentCertificateQueryDTO dto = new TalentCertificateQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        List<TalentCertificateVO> list = PageUtil.getAllList(dto, d -> talentCertificateService.queryPage(d));
        return list.size();
    }

    @Override
    public Pair<Integer, Integer> getEnterpriseTalentCountTotal() {
        List<TalentVO> talentList = PageUtil.getAllList(new TalentQueryDTO(), d -> talentService.queryPage(d));
        List<CustomerInfoVO> customerList = PageUtil.getAllList(new CustomerInfoQueryDTO(), d -> customerInfoService.queryPage(d));
        return Pair.of(talentList.size(), customerList.size());
    }
}
