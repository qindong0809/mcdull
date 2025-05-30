package io.gitee.dqcer.mcdull.business.common.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.IOException;

/**
 * 右页脚处理程序
 *
 * @author dqcer
 * @since 2023/03/02
 */
public class RightFooterHandler implements IEventHandler {

    protected PdfFormXObject placeholder;
    protected float side = 20;

    protected float x = 553;

    protected float y = 8;
    protected float space = 4.5f;
    protected float descent = 3;


    public RightFooterHandler() {
        placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdfDoc.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();
        float pdfHeight = pageSize.getHeight();

        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdfDoc);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        // 边框处理
        Div div = new Div();
        div.setHeight(pdfHeight - 55);
        div.setMarginTop(30F);
        div.setWidth(pageSize.getWidth() - 40);
        div.setHorizontalAlignment(HorizontalAlignment.CENTER);
        div.setBorder(new SolidBorder( 0.1f));
        canvas.add(div);

//        Table table = new Table(new float[]{1, 1, 1, 1});
//        table.setMargins(0, 0, 0, 0);
//        table.useAllAvailableWidth();
//        table.setFixedLayout();
//        table.setBorder(Border.NO_BORDER);
//        table.setBorder(new SolidBorder( 0.1f));
//        table.setFontSize(12);
//
//        Cell cell = newCell(null, true, true, new Integer[]{1, 2});
//        cell.setVerticalAlignment(VerticalAlignment.TOP);
//        Paragraph paragraph = new Paragraph();
//        paragraph.add(new Paragraph("blaze").setFontSize(10).setMarginLeft(24).setTextAlignment(TextAlignment.LEFT));
//        cell.add(paragraph);
//
//        Cell cell1 = newCell(null, true, true, new Integer[]{2, 2});
////        cell.setVerticalAlignment(VerticalAlignment.TOP);
//        Paragraph paragraph1 = new Paragraph();
//        paragraph1.add(new Paragraph("22222").setFontSize(10).setMarginLeft(24).setTextAlignment(TextAlignment.LEFT));
//        cell1.add(paragraph1);
//
//        table.addCell(cell);
//        table.addCell(cell1);
//        div.add(table);
//
//        canvas.add(div);

        // 右页脚
        Paragraph rightParagraph = new Paragraph("Page " + pageNumber + " of").setFontSize(10f);
        canvas.showTextAligned(rightParagraph, x, y, TextAlignment.RIGHT);

        // 右页眉
        Paragraph rightHeaderParagraph = new Paragraph("四象合创").setFontSize(10f);
        canvas.showTextAligned(rightHeaderParagraph, x, pdfHeight -24, TextAlignment.RIGHT);

        // 为分页准备的占位符
        pdfCanvas.addXObject(placeholder, x + space, y - descent);
        canvas.close();
    }

    public void writeTotal(PdfDocument pdf) {
        Canvas canvas = new Canvas(placeholder, pdf);
        Paragraph leftParagraph = null;
        try {
            leftParagraph = new Paragraph(String.valueOf(pdf.getNumberOfPages())).setFont(PdfFontFactory.createFont()).setFontSize(10f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        canvas.showTextAligned(leftParagraph, 0, descent, TextAlignment.LEFT);
    }

    public static Cell newCell(Border border, Boolean keepTogether, Boolean setMinHeight, Integer[] span) {
        Cell cell;
        if (span != null) {
            cell = new Cell(span[0], span[1]);
        } else {
            cell = new Cell();
        }

        cell.setBorder(Border.NO_BORDER).setFontSize(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (border != null) {
            cell.setBorder(border);
        }
        if (keepTogether != null) {
            cell.setKeepTogether(keepTogether);
        }

        if (setMinHeight != null && setMinHeight) {
            cell.setMinHeight(16);
        }
        return cell;
    }
}
