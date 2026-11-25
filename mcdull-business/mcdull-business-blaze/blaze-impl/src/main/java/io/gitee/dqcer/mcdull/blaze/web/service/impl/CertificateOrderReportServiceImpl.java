package io.gitee.dqcer.mcdull.blaze.web.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.CertificateRequirementsVO;
import io.gitee.dqcer.mcdull.blaze.web.service.IBlazeOrderDetailService;
import io.gitee.dqcer.mcdull.blaze.web.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.blaze.web.service.ICertificateOrderReportService;
import io.gitee.dqcer.mcdull.blaze.web.service.ICertificateRequirementsService;
import io.gitee.dqcer.mcdull.common.pdf.ByteArrayInOutConvert;
import io.gitee.dqcer.mcdull.common.pdf.HtmlConvertPdf;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.json.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 证书需求与订单明细的跨服务报表编排实现。
 */
@Service
public class CertificateOrderReportServiceImpl implements ICertificateOrderReportService {

    @Resource
    private ICertificateRequirementsService certificateRequirementsService;
    @Resource
    private IBlazeOrderService orderService;
    @Resource
    private IBlazeOrderDetailService orderDetailsService;

    @Override
    public void getExportCustomerOrderDetailPdf(Integer customerId) throws IOException {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        dto.setId(customerId);
        PagedVO<CertificateRequirementsVO> certificateRequirementsVOPagedVO = certificateRequirementsService.queryPage(dto);
        if (ObjUtil.isNotNull(certificateRequirementsVOPagedVO)) {
            List<CertificateRequirementsVO> list = certificateRequirementsVOPagedVO.getList();
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(list)) {
                CertificateRequirementsVO vo = list.get(0);
                Map<String, String> map = new HashMap<>();
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getCustomerName), vo.getCustomerName());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getResponsibleUserIdStr), vo.getResponsibleUserIdStr());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getResponsibleUserPhone), vo.getResponsibleUserPhone());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getResponsibleUserDepartmentName), vo.getResponsibleUserDepartmentName());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getCertificateLevelName), vo.getCertificateLevelName());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getSpecialtyName), vo.getSpecialtyName());
                map.put(LambdaUtil.getFieldName(CertificateRequirementsVO::getSocialSecurityRequirementName), vo.getSocialSecurityRequirementName());
                String body = "";

                BlazeOrderQueryDTO orderDto = new BlazeOrderQueryDTO();
                orderDto.setCustomerCertId(vo.getId());
                PagedVO<BlazeOrderVO> voPagedVO = orderService.queryPage(orderDto);
                if (ObjUtil.isNotNull(voPagedVO)) {
                    List<BlazeOrderVO> orderList = voPagedVO.getList();
                    if (cn.hutool.core.collection.CollUtil.isNotEmpty(orderList)) {
                        for (BlazeOrderVO orderVO : orderList) {
                            body += " <div class=\"descriptions\">\n" +
                                    "                    <div class=\"descriptions-title\">匹配人才订单号:" + orderVO.getOrderNo() + "</div>\n" +
                                    "                    <div class=\"descriptions-content\">\n" +
                                    "                        <div class=\"descriptions-item\">\n" +
                                    "                            <div class=\"descriptions-label\">对接人</div>\n" +
                                    "                            <div class=\"descriptions-value\">" + orderVO.getTalentResponsibleUserIdStr() + "</div>\n" +
                                    "                        </div>\n" +
                                    "                        <div class=\"descriptions-item\">\n" +
                                    "                            <div class=\"descriptions-label\">电话</div>\n" +
                                    "                            <div class=\"descriptions-value\">" + orderVO.getTalentResponsibleUserPhone() + "</div>\n" +
                                    "                        </div>\n" +
                                    "                        <div class=\"descriptions-item\">\n" +
                                    "                            <div class=\"descriptions-label\">所属</div>\n" +
                                    "                            <div class=\"descriptions-value\">" + orderVO.getTalentResponsibleDepartment() + "</div>\n" +
                                    "                        </div>\n" +
                                    "                        <div class=\"descriptions-item\">\n" +
                                    "                            <div class=\"descriptions-label\">人才</div>\n" +
                                    "                            <div class=\"descriptions-value\">" + orderVO.getTalentCertName() + "</div>\n" +
                                    "                        </div>\n" +
                                    "                    </div>\n" +
                                    "                </div> <br /> <br /> <br />";
                        }
                        List<Integer> orderIdList = orderList.stream().map(BlazeOrderVO::getId).collect(Collectors.toList());
                        BlazeOrderDetailQueryDTO detailQueryDTO = new BlazeOrderDetailQueryDTO();
                        detailQueryDTO.setOrderIdList(orderIdList);
                        detailQueryDTO.setIsTalent(false);
                        PagedVO<BlazeOrderDetailVO> orderDetailVOPagedVO = orderDetailsService.queryPage(detailQueryDTO);
                        if (ObjUtil.isNotNull(orderDetailVOPagedVO)) {
                            List<BlazeOrderDetailVO> orderDetailList = orderDetailVOPagedVO.getList();
                            if (cn.hutool.core.collection.CollUtil.isNotEmpty(orderDetailList)) {
                                StringJoiner joiner = new StringJoiner("。<br /> <br />");
                                for (BlazeOrderDetailVO orderDetailVO : orderDetailList) {
                                    String date = TimeZoneUtil.serializeDate(orderDetailVO.getOperationTimeStr(), null, null, null, null, false);
                                    String format = StrUtil.format("日期：{} 操作人【{}】收到回款【{}】元", date, orderDetailVO.getResponsibleUserName(), orderDetailVO.getPrice());
                                    joiner.add(format);
                                }
                                body += joiner.toString();
                            }
                        }
                    }
                }
                String html = HtmlConvertPdf.getHtml(body, "pdf.html");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    html = StrUtil.replace(html, StrUtil.format("${{}}", entry.getKey()), entry.getValue());
                }
                html = StrUtil.format(html, body);
                ByteArrayInOutConvert byteArrayInOutStream = new HtmlConvertPdf().generatePdf(html);
                String message = TimeZoneUtil.serializeDate(new Date(), "yyyyMMdd_HHmmss", false);
                String fileName = StrUtil.format("{}_{}_{}_{}.pdf", vo.getCustomerName(), vo.getCertificateLevelName(), vo.getSpecialtyName(), message);
                String pafPathParent = FileUtil.getTmpDirPath() + java.io.File.separator + System.currentTimeMillis();
                String pdfPath = pafPathParent + java.io.File.separator + fileName;
                HtmlConvertPdf.updatePdfLeftFooter(byteArrayInOutStream.getInputStream(), pdfPath, message);
                ServletUtil.download(fileName, FileUtil.readBytes(pdfPath));
            }
        }
    }
}
