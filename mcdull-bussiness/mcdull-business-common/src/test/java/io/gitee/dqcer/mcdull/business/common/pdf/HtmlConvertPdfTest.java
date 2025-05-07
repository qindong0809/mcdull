package io.gitee.dqcer.mcdull.business.common.pdf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

class HtmlConvertPdfTest {

//    @Test
    void generatePdf() {

        String pdf = "D:\\temp\\1-1-1-1-3-1.pdf";
        ArrayList<String> objects = new ArrayList<>();
        objects.add("administrator@keda.com");
        objects.add("administrator@keda.com");
        objects.add("administrator@keda.com");
        objects.add("administrator@keda.com");
        objects.add("administrator@keda.com");
        objects.add("administrator@keda.com");

        ArrayList<String> list = new ArrayList<>();
        list.add("hr@keda.com");
        list.add("dev@keda.com");
        list.add("dev@keda.com");
        list.add("dev@keda.com");
        list.add("dev@keda.com");
        list.add("dev@keda.com");
        ByteArrayInOutConvert byteArrayInOutStream = new HtmlConvertPdf()
                .generatePdf(HtmlConvertPdf.getHtml("Email subject", objects, list, new Date(), "Hello word!!!"));
        FileUtil.writeBytes(byteArrayInOutStream.toByteArray(), pdf);

        // 第二次更新pdf
        String footerLeftMsgFormat = "Report generated for {0} on {1}";
        String message = MessageFormat.format(footerLeftMsgFormat, "Terry",
                DateUtil.formatDateTime(new Date()));
        HtmlConvertPdf.updatePdfLeftFooter(FileUtil.getInputStream(pdf), pdf + ".pdf", message);

    }
}