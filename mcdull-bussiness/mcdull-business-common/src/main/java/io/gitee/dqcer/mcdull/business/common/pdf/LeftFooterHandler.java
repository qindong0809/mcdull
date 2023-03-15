package io.gitee.dqcer.mcdull.business.common.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;


/**
 * 左页脚处理程序
 *
 * @author dqcer
 * @since 2023/03/02
 */
public class LeftFooterHandler implements IEventHandler {

    protected float x = 553;

    protected float y = 8;
    protected float space = 4.5f;
    protected float descent = 3;

    private String footerLeftMsg;

    public LeftFooterHandler(String footerLeftMsg) {
        this.footerLeftMsg = footerLeftMsg;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();

        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdfDoc);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        // 左页脚
        Paragraph leftParagraph = new Paragraph(footerLeftMsg).setFontSize(10f);
        canvas.showTextAligned(leftParagraph, 25, y, TextAlignment.LEFT);

        canvas.close();
    }


}
