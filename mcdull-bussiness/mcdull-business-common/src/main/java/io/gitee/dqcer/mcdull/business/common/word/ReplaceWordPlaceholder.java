package io.gitee.dqcer.mcdull.business.common.word;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 替换word占位符
 *
 * @author dqcer
 * @since 2023/05/07 17:05:59
 */
public class ReplaceWordPlaceholder {

    public static void main(String[] args) {
        //替换文本
        Map<String, Object> data = new HashMap<>();
        //替换图片
        Map<String, Object> picData = new HashMap<>();
        data.put("${number}", "ZJZB-FZC-2021062");
        data.put("${userName}", "张三");
        data.put("${org}", "apache");
        data.put("${time}", DateUtil.formatChineseDate(new Date(), true, true));
        data.put("${addres}", "成都市");
//        data.put("${test}", "表格外的文本");
        //指定图片地址
//        picData.put("${pic}", "C:\\Users\\admin\\Desktop\\路飞.jpg");


        // resource 读取报错，暂未解决，怀疑编码格式问题
        InputStream stream = ResourceUtil.getStream("D:\\temp\\template.docx");

        byte[] output = operateWord(data, picData, stream);
        assert output != null;
        FileUtil.writeBytes(output, "D:\\temp\\1-5.docx");

    }
    public static byte[] operateWord(Map<String, Object> data, Map<String, Object> picData, InputStream stream ){
        try {
            XWPFDocument document = new XWPFDocument(stream);
            if (data.size() > 0) {
                // 替换掉表格之外的文本(仅限文本)
                ReplaceWordPlaceholder.changeText(document, data);
                // 替换表格内的文本对象
                ReplaceWordPlaceholder.changeTableText(document, data);
            }
            if (picData.size() > 0) {
                // 替换内容图片
                ReplaceWordPlaceholder.changePic(document, picData);
                // 替换表格内的图片对象
                ReplaceWordPlaceholder.changeTablePic(document, picData);
            }
            //导出替换后的文件
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 替换段落文本
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeText(XWPFDocument document, Map<String, Object> textMap) {
        // 获取段落集合
        Iterator<XWPFParagraph> iterator = document.getParagraphsIterator();
        XWPFParagraph paragraph = null;
        while (iterator.hasNext()) {
            paragraph = iterator.next();
            // 判断此段落是否需要替换
            if (checkText(paragraph.getText())) {
                replaceValue(paragraph, textMap);
            }
        }
    }

    /***
     *  检查文本中是否包含指定的字符(此处为“$”)
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.contains("$")) {
            check = true;
        }
        return check;
    }

    /**
     * 替换图片
     *
     */
    public static void changePic(XWPFDocument document, Map<String, Object> picData) throws Exception {
        // 获取段落集合
        Iterator<XWPFParagraph> iterator = document.getParagraphsIterator();
        XWPFParagraph paragraph;
        while (iterator.hasNext()) {
            paragraph = iterator.next();
            // 判断此段落是否需要替换
            String text = paragraph.getText();
            if (checkText(text)) {
                replacePicValue(paragraph, picData);
            }
        }
    }

    /***
     *  替换表格内的文字
     */
    public static void changeTableText(XWPFDocument document, Map<String, Object> data) {
        // 获取文件的表格
        Iterator<XWPFTable> tableList = document.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        // 循环所有需要进行替换的文本，进行替换
        while (tableList.hasNext()) {
            table = tableList.next();
            if (checkText(table.getText())) {
                rows = table.getRows();
                // 遍历表格，并替换模板
                for (XWPFTableRow row : rows) {
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 判断单元格是否需要替换
                        if (checkText(cell.getText())) {
                            List<XWPFParagraph> paragraphs = cell.getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                replaceValue(paragraph, data);
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * 替换表格内图片
     */
    public static void changeTablePic(XWPFDocument document, Map<String, Object> picData) throws Exception {
        // 获取文件的表格
        Iterator<XWPFTable> tableList = document.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        // 循环所有需要进行替换的文本，进行替换
        while (tableList.hasNext()) {
            table = tableList.next();
            if (checkText(table.getText())) {
                rows = table.getRows();
                // 遍历表格，并替换模板
                for (XWPFTableRow row : rows) {
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 判断单元格是否需要替换
                        if (checkText(cell.getText())) {
                            List<XWPFParagraph> paragraphs = cell.getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                replacePicValue(paragraph, picData);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void replaceValue(XWPFParagraph paragraph, Map<String, Object> textMap) {
        XWPFRun run, nextRun;
        String runsText;
        List<XWPFRun> runs = paragraph.getRuns();
        for (int i = 0; i < runs.size(); i++) {
            run = runs.get(i);
            runsText = run.getText(0);
            if (runsText == null) {
                continue;
            }
            if (runsText.contains("${") || (runsText.contains("$") && runs.get(i + 1).getText(0).substring(0, 1).equals("{"))) {
                while (!runsText.contains("}")) {
                    nextRun = runs.get(i + 1);
                    runsText = runsText + nextRun.getText(0);
                    //删除该节点下的数据
                    paragraph.removeRun(i + 1);
                }
                runsText = runsText.trim();
                Object value = changeValue(runsText, textMap);
                //判断key在Map中是否存在
                if (textMap.containsKey(runsText)) {
                    run.setText(value.toString(), 0);
                } else {
                    //如果匹配不到，则不修改
                    run.setText(runsText, 0);
                }
            }
        }
    }

    /***
     *  替换图片内容
     */
    public static void replacePicValue(XWPFParagraph paragraph, Map<String, Object> picData) throws Exception {
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            Object value = changeValue(run.toString(), picData);
            if (picData.containsKey(run.toString())) {
                //清空内容
                run.setText("", 0);
                FileInputStream is = new FileInputStream((String) value);
                //图片宽度、高度
                int width = Units.toEMU(100), height = Units.toEMU(100);
                //添加图片信息，段落高度需要在模板中自行调整
                run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, (String) value, width, height);
            }
        }
    }

    /***
     *  匹配参数
     */
    public static Object changeValue(String value, Map<String, Object> textMap) {
        Object valu = "";
        for (Map.Entry<String, Object> textSet : textMap.entrySet()) {
            // 匹配模板与替换值 格式${key}
            String key = textSet.getKey();
            if (value.contains(key)) {
                valu = textSet.getValue();
            }
        }
        return valu;
    }
}
