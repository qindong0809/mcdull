package io.gitee.dqcer.mcdull.admin.util.pdf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import io.gitee.dqcer.mcdull.framework.mysql.config.CustomIdGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

/**
 * html转换pdf
 *
 * @author dqcer
 * @since  2023/02/06
 */
public class HtmlConvertPdfUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomIdGenerator.class);

    public static String readTemplate(String templateName) throws IOException {
        URL headerResource = HtmlConvertPdfUtil.class.getClassLoader().getResource(templateName);
        if (isNull(headerResource)) {
            throw new FileNotFoundException("Header template is not found");
        }
        return IOUtils.toString(headerResource, UTF_8);
    }


    public static void generatePdfFile(String content, String pdfFile, String css) throws Exception {
        OutputStream outputStream = new FileOutputStream(pdfFile);

        InputStream cssInputStream = new ByteArrayInputStream(css.getBytes());
        // 创建document对象
        Rectangle rectangle = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        Document document = new Document(rectangle);
        document.setMargins(50, 50, 100, 60);
        // 创建writer对象
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);


        Image image = null;
        String leftFooter = "Report generated for %s on %s";
        String leftHeader = null;
        String rightHeader = null;

        writer.setPageEvent(new HeaderFooterHandler(null, null, image, leftHeader, rightHeader, leftFooter));
        // 打开document对象
        document.open();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());


        XMLWorkerHelper.getInstance().parseXHtml(writer, document, byteArrayInputStream, cssInputStream, StandardCharsets.UTF_8, new AsianFontProvider());
        document.close();
        byteArrayInputStream.close();
    }

    public static String html(String subject, List<String> sendTos, List<String> ccs, Date sendTime, String body) throws IOException {
        String emailHtmlTemplate = readTemplate("email_template.html");
        return StrUtil.format(emailHtmlTemplate, subject, String.join(",", sendTos),
                String.join(",", ccs),"20230207", body);
    }

    public static String createFile(String directoryPath, String pdfFile) {
        File file = new File(directoryPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String filePath = directoryPath + "\\" + pdfFile;
        File file2 = new File(filePath);
        if (!file2.exists()){
            //创建文件
            try {
                if (file2.createNewFile()){
                    //写入内容
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return filePath;
    }


    public static void main(String[] args) throws Exception {
        String defaultBaseDir = System.getProperty("java.io.tmpdir");

        System.out.println(File.separator);
        String directoryPath = defaultBaseDir  + "email_"  + "_" + "bizName";
        String filePah = RandomUtil.randomNumbers(32) + ".pdf";
        String targetFilePath = createFile(directoryPath, filePah);

        List<String> list = new ArrayList<>();
        list.add("demo@qq.com");
        String str = html("subject", list, list, new Date(), "body");
        generatePdfFile(str, targetFilePath, "");
        log.info("pdf 已生成： {}", targetFilePath);

    }
}
