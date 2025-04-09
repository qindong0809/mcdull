package io.gitee.dqcer.mcdull.business.common.pdf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;


public class HtmlConvertPdf {

    public static final String EMAIL_TEMPLATE = "email_letter.html";


    private static final Logger log = LoggerFactory.getLogger(HtmlConvertPdf.class);


    /**
     * 更新pdf的左页脚
     *
     * @param inputStream 输入流
     * @param pdfPath     pdf文件路径
     * @param message     消息
     */
    public static void updatePdfLeftFooter(InputStream inputStream, String pdfPath, String message) {
        FileUtil.touch(pdfPath);
        updatePdfLeftFooter(inputStream, FileUtil.getOutputStream(pdfPath), message);
    }

    /**
     * 更新pdf的左页脚
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param message      消息
     */
    public static void updatePdfLeftFooter(InputStream inputStream, OutputStream outputStream, String message) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfReader(inputStream),new PdfWriter(outputStream));
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new LeftFooterHandler(message));
            Document doc = new Document( pdfDocument);
            doc.close();
            pdfDocument.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据html生成pdf
     *
     * @param htmlStr html str
     * @return {@link ByteArrayInOutConvert}
     */
    public ByteArrayInOutConvert generatePdf(String htmlStr) {
        ByteArrayInOutConvert target = new ByteArrayInOutConvert();
        try {
            PdfWriter writer = new PdfWriter(target);
            PdfDocument pdfDocument = new PdfDocument(writer);
            RightFooterHandler rightFooterHandler = new RightFooterHandler();
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, rightFooterHandler);
            ConverterProperties converterProperties = new ConverterProperties();
            // 添加字体
            converterProperties.setFontProvider(FontUtils.getFontProvider());
            Document doc = HtmlConverter.convertToDocument(new ByteArrayInputStream(htmlStr.getBytes()), pdfDocument, converterProperties);
            doc.flush();
            rightFooterHandler.writeTotal(pdfDocument);
            doc.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return target;
    }

    public static String getHtml(String subject, List<String> sendTos, List<String> ccs, Date sendTime, String body) {
        ClassPathResource resource = new ClassPathResource(EMAIL_TEMPLATE);
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        String htmlFormat = IoUtil.readUtf8(stream);
        return StrUtil.format(htmlFormat, subject, String.join(",", sendTos),
                String.join(",", ccs) , DateUtil.formatDateTime(sendTime), body);
    }

    public static String getHtml( String body, String templateName) {
        ClassPathResource resource = new ClassPathResource(templateName);
        InputStream stream = null;
        try {
            stream = resource.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return IoUtil.readUtf8(stream);
    }
}
