package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.blaze.domain.vo.ReportViewVO;
import io.gitee.dqcer.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.blaze.service.IReportService;
import io.gitee.dqcer.blaze.service.ITalentCertificateService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
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

    @Override
    public ReportViewVO getReportView() {
        CertificateRequirementsQueryDTO certificateRequirementsQueryDTO = new CertificateRequirementsQueryDTO();
        PageUtil.setMaxPageSize(certificateRequirementsQueryDTO);
        PagedVO<CertificateRequirementsVO> page = certificateRequirementsService.queryPage(certificateRequirementsQueryDTO);
        Map<String, Long> customerMap = new HashMap<>();
        List<CertificateRequirementsVO> customerList = new ArrayList<>();
        if (ObjUtil.isNotNull(page)) {
            List<CertificateRequirementsVO> list = page.getList();
            if (CollUtil.isNotEmpty(list)) {
                // 根据certificateLevelName + specialtyName 分组
                customerMap = list.stream().collect(Collectors
                        .groupingBy(item -> item.getCertificateLevelName() + " + " + item.getSpecialtyName(), Collectors.counting()));
            }
        }
        TalentCertificateQueryDTO talentCertificateQueryDTO = new TalentCertificateQueryDTO();
        PageUtil.setMaxPageSize(talentCertificateQueryDTO);
        PagedVO<TalentCertificateVO> talentCertificateVOPagedVO = talentCertificateService.queryPage(talentCertificateQueryDTO);
        Map<String, Long> talentMap = new HashMap<>();
        if (ObjUtil.isNotNull(talentCertificateVOPagedVO)) {
            List<TalentCertificateVO> list = talentCertificateVOPagedVO.getList();
            if (CollUtil.isNotEmpty(list)) {
                // 根据certificateLevelName + specialtyName 分组
                talentMap = list.stream().collect(Collectors
                        .groupingBy(item -> item.getCertificateLevelName() + " + " + item.getSpecialtyName(), Collectors.counting()));
            }
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
}
