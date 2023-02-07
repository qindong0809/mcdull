package io.gitee.dqcer.mcdull.admin.util.pdf;

import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

/**
 * 页眉页脚处理
 *
 * @author dqcer
 * @date 2023/02/07
 */
public class HeaderFooterHandler extends PdfPageEventHelper {

    private static final int FONT_SIZE = 10;
    private Integer activeStatus;
    private Integer templateType;
    private Font bfChinese;
    private Image leftHeaderImg;
    private String leftHeader;
    private String rightHeader;
    private String leftFooter;
    private PdfTemplate pdfTemplate;

    {
        try {
            bfChinese = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), FONT_SIZE, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public HeaderFooterHandler(Integer activeStatus, Integer templateType, Image leftHeaderImg, String leftHeader, String rightHeader, String leftFooter) {
        this.activeStatus = activeStatus;
        this.templateType = templateType;
        this.leftHeaderImg = leftHeaderImg;
        this.leftHeader = leftHeader;
        this.rightHeader = rightHeader;
        this.leftFooter = leftFooter;
    }

    /***
     *  页面打开时添加水印
     *
     */
    @Override
    public void onStartPage(PdfWriter writer, Document document) {

        if (null != templateType && templateType == 3 && null != activeStatus && activeStatus < 7) {
//            addWaterMark(writer, document);
        }
    }

    /***
     *  文档打开时设置页眉图片属性
     *
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
//        super.onOpenDocument(writer,document);
        pdfTemplate = writer.getDirectContent().createTemplate(300, 100);
        /*try {
            if (leftHeaderImg != null) {
                leftHeaderImg.scaleAbsoluteHeight(40);
                leftHeaderImg.scaleAbsoluteWidth(60);
                leftHeaderImg.setAbsolutePosition(50, document.top() + 20);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    private void addWaterMark(PdfWriter writer, Document document) {
        try {
            // 加入水印
            PdfContentByte waterMar = writer.getDirectContentUnder();
            // 开始设置水印
            waterMar.beginText();
            // 设置水印透明度
            PdfGState gs = new PdfGState();
            // 设置填充字体不透明度为0.2f
            gs.setFillOpacity(0.2f);
            // 设置水印字体参数及大小
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.EMBEDDED);
            waterMar.setFontAndSize(baseFont, 30);
            // 设置透明度
            waterMar.setGState(gs);
            // 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
            for (int i = 1; i <= 5; i += 2) {
                for (int j = 2; j <= 8; j += 3) {
                    waterMar.showTextAligned(Element.ALIGN_RIGHT, "draft", i * 150, j * 90, 45);
                }
            }
            //结束设置
            waterMar.endText();
            waterMar.stroke();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  每页关闭时添加页脚
     *
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        // 左页眉文字
        if(leftHeaderImg == null){
            // 没有图片
            setLeftHeaderWithoutImg(writer,document,leftHeader);
        }else{
            // 有图片
            setLeftHeader(writer, document,leftHeader);
        }
        /*ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                new Phrase(leftHeader, bfChinese),
                (document.left()),
                document.top()+ 30,
                0);*/

        // 右页眉文字
        setRightHeader(writer, document,rightHeader);
       /* ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                new Phrase(rightHeader, bfChinese),
                (document.right()),
                document.top() + 40, 0);*/

        // 左页脚文字
        setLeftFooter(writer, document,leftFooter);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                new Phrase(leftFooter, bfChinese),
                (document.left()),
                document.bottom() - 20,
                0);

        int pageS = writer.getPageNumber();
        String pageName = String.format("Page %s of", pageS);
        Phrase footer = new Phrase(pageName, bfChinese);

        // 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
        float len = bfChinese.getBaseFont().getWidthPoint(pageName, FONT_SIZE);

        // 4.拿到当前的PdfContentByte
        PdfContentByte cb = writer.getDirectContent();

        // 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F
        // 再给偏移20F适合人类视觉感受，否则肉眼看上去就太偏左了
        // ,y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的。
        ColumnText
                .showTextAligned(
                        cb,
                        Element.ALIGN_CENTER,
                        footer,
                        (document.right() - bfChinese.getBaseFont().getWidthPoint(String.valueOf(footer), FONT_SIZE) - 30),
                        document.bottom() - 20, 0);

        // 6.调节模版显示的位置
        cb.addTemplate(pdfTemplate, (document.right() - len - 20),
                document.bottom() - 20);
        try {
            if (leftHeaderImg != null) {
                if(StrUtil.isNotEmpty(leftHeader)){
                    leftHeaderImg.scaleAbsoluteHeight(50);
                    leftHeaderImg.scaleAbsoluteWidth(70);
                    leftHeaderImg.setAbsolutePosition(document.left(), document.top() + 30);
                    writer.getDirectContent().addImage(leftHeaderImg);
                }else{
                    leftHeaderImg.scaleAbsoluteHeight(60);
                    leftHeaderImg.scaleAbsoluteWidth(70);
                    leftHeaderImg.setAbsolutePosition(document.left(), document.top() + 20);
                    writer.getDirectContent().addImage(leftHeaderImg);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        //关闭document的时候获取总页数，并把总页数按模版写道之前预留的位置
        pdfTemplate.beginText();
        pdfTemplate.setFontAndSize(bfChinese.getBaseFont(), 10f);
        pdfTemplate.showText(String.valueOf(writer.getPageNumber()));
        pdfTemplate.endText();
        pdfTemplate.closePath();//sanityCheck();
    }

    private void setRightHeader(PdfWriter writer, Document document,String rightHeader){
        Integer offset = 70;
        if(StrUtil.isNotEmpty(rightHeader)){
            String[] str = rightHeader.split("\n");
            for (String s : str) {
                // 右页眉文字
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                        new Phrase(s, bfChinese),
                        (document.right()),
                        document.top() + offset, 0);
                offset = offset -10;
                if(offset <= 10){
                    break;
                }
            }
        }
    }

    private void setLeftHeader(PdfWriter writer, Document document,String leftHeader){
        Integer offset = 20;
        if(StrUtil.isNotEmpty(leftHeader)){
            String[] str = leftHeader.split("\n");
            for (String s : str) {
                // 右页眉文字
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                        new Phrase(s, bfChinese),
                        (document.left()),
                        document.top() + offset, 0);
                offset = offset -10;
                if(offset <= 0){
                    break;
                }
            }
        }
    }

    private void setLeftHeaderWithoutImg(PdfWriter writer, Document document,String leftHeader){
        Integer offset = 70;
        if(StrUtil.isNotEmpty(leftHeader)){
            String[] str = leftHeader.split("\n");
            for (String s : str) {
                // 右页眉文字
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                        new Phrase(s, bfChinese),
                        (document.left()),
                        document.top() + offset, 0);
                offset = offset -10;
                if(offset <= 0){
                    break;
                }
            }
        }
    }
    private void setLeftFooter(PdfWriter writer, Document document,String leftFooter){
        Integer offset = 20;
        if(StrUtil.isNotEmpty(leftFooter)){
            String[] str = leftFooter.split("\n");
            for (String s : str) {
                // 右页眉文字
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
                        new Phrase(s, bfChinese),
                        (document.left()),
                        document.bottom() - offset, 0);
                offset = offset + 10;
                if(offset >=60){
                    break;
                }
            }
        }
    }
}
