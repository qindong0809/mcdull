package io.gitee.dqcer.mcdull.gateway;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.OutlineHandler;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfTrueTypeFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.InsetBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EnumSet;

public class HtmlConvertPdfUtil {

    public static void main() throws IOException {

    }

    public static void main(String[] args) throws Exception {
        String html = "D:\\temp\\1.html";
        String pdf = "D:\\temp\\1-1-1-1.pdf";
        // IO
//        File htmlSource = new File(html);
//        File pdfDest = new File(pdf);
        // pdfHTML specific code
//        ConverterProperties converterProperties = new ConverterProperties();
//        HtmlConverter.convertToPdf(new FileInputStream(htmlSource),
//                new FileOutputStream(pdfDest), converterProperties);
       new HtmlConvertPdfUtil().generatePdf(html, pdf);
    }

    public void generatePdf(String html, String outputFile) {
        try {
            //outputFile也可以是输出流
            PdfWriter writer = new PdfWriter(outputFile);
            PdfDocument pdfDocument = new PdfDocument(writer);




            //Create event-handlers
            PageXofY footerHandler = new PageXofY(pdfDocument);
            //Assign event-handlers
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
            //Convert
            ConverterProperties converterProperties = new ConverterProperties().setBaseUri("D:\\temp");
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document doc = HtmlConverter.convertToDocument(new FileInputStream(new File(html).getAbsolutePath()), pdfDocument, converterProperties);
            //Write the total number of pages to the placeholder

            doc.flush();
            footerHandler.writeTotal(pdfDocument);


//            footerHandler.closeDocument(pdfDocument);
            doc.close();
        } catch (Exception e) {

        }
    }

    protected class PageXofY implements IEventHandler {
        private final static String logoPath = "D:\\default_logo.png";

        protected PdfFormXObject placeholder;
        protected float side = 20;
//        protected float x = 300;
        protected float x = 553;
        protected float y = 8;
        protected float space = 4.5f;
        protected float descent = 3;

        public PageXofY(PdfDocument pdf) {
            placeholder =
                    new PdfFormXObject(new Rectangle(0, 0, side, side));
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            Document doc = new Document(pdfDoc);
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            float pdfHeight = pageSize.getHeight();

//            try {
//                this.addPageHeader(doc, pdfHeight);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdfDoc);
            Canvas canvas = new Canvas(pdfCanvas, pageSize);

            try {
                Div div = new Div();
                div.setHeight(pdfHeight - 48);
                div.setMarginTop(20F);
                div.setWidth(pageSize.getWidth() - 40);
//                div.setVerticalAlignment(VerticalAlignment.MIDDLE);
                div.setHorizontalAlignment(HorizontalAlignment.CENTER);
                div.setBorder(new SolidBorder( 0.1f));

//                div.setFixedPosition(8f, PageSize.LETTER.getHeight() - 27 - 57-15, 8);
                //div.setFixedPosition(100,100,100, 56);

//                Div d = new Div();
//                d.setHeight(50);
//                d.setWidth(pageSize.getWidth() - 80);
//                d.setBorderBottom(new SolidBorder( 0.1f));
//                d.setHorizontalAlignment(HorizontalAlignment.LEFT);
//               d.add(new Paragraph("Options").setFontSize(10).setMarginBottom(10));
//
//                div.add(d);

                canvas.add(div);


                // 左页脚
                Paragraph leftParagraph = new Paragraph("Report for (name) on 10-Apr-2021").setFont(PdfFontFactory.createFont()).setFontSize(10f);
                canvas.showTextAligned(leftParagraph, 175, y, TextAlignment.RIGHT);

                // 右页脚
                Paragraph rightParagraph = new Paragraph("Page " + pageNumber + " of").setFont(PdfFontFactory.createFont()).setFontSize(10f);
                canvas.showTextAligned(rightParagraph, x, y, TextAlignment.RIGHT);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            pdfCanvas.addXObjectAt(placeholder, x + space, y - descent);
            canvas.close();
        }

        private void addPageHeader(Document document, float pdfHeight) throws IOException {
            //创建字体
//            PdfFont textFont = new PdfTrueTypeFont(BASE_FONT, 10f);
            PdfFont textFont = PdfFontFactory.createFont();

            float width = PageSize.A4.getWidth()-60;
            //表格 一行两列
            Table table = new Table(2);
            table.setHeight(40);
            table.setWidth(width);

            //logo
            Image logo = new Image(ImageDataFactory.create(logoPath));
            table.addCell(new Cell().add(logo).setHeight(4).setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.TOP));

            //名称
            Paragraph nameP = new Paragraph("test").setFont(textFont).setFontSize(10f);
            table.addCell(new Cell().add(nameP).setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.RIGHT).setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));

            //设置表格的位置 页眉处
            table.setFixedPosition(document.getLeftMargin()-10, pdfHeight- document.getTopMargin()-5, table.getWidth());
            document.add(table);
        }
        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            Paragraph leftParagraph = null;
            try {
                leftParagraph = new Paragraph(String.valueOf(pdf.getNumberOfPages())).setFont(PdfFontFactory.createFont()).setFontSize(10f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            canvas.setBackgroundColor(ColorConstants.RED);
            canvas.showTextAligned(leftParagraph, 0, descent, TextAlignment.LEFT);
        }
    }
}
