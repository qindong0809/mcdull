package io.gitee.dqcer.mcdull.business.common;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.util.StringUtils;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;

import java.util.ArrayList;
import java.util.List;

public final class FileNameGeneratorUtil {
    private static final String EXCEL = ".xlsx";

    public static String simple(String name) {
        String formatted = DateUtil.formatDateTime(UserContextHolder.getSession().getNow());
        final String format = "%s-%s" + EXCEL;
        return String.format(format, name, formatted);
    }

    public static void main(String[] args) {
        List<String> list =new ArrayList<>();
        list.add("文件夹(1)(1)");
        list.add("文件夹(3)");
        list.add("文件夹(2)");
        list.add("文件夹(1)");
        list.add("文件夹");
        String fileName = "文件夹";
        fileName = buildName(list, fileName, 1);
        System.out.println("保存起来的文件名称为 :" + fileName);
    }

    private static String buildName(List<String> list, String fileName, int initIndex){
        if (!list.contains(fileName)) {
            return fileName;
        }
        if (decide(fileName)) {
            return fileName = buildName(list, fileName + "(" + initIndex + ")", initIndex++);
        }
        String numCountString = fileName.substring(fileName.lastIndexOf("(") + 1, fileName.lastIndexOf(")"));
        int numCount = -1;
        if (StringUtils.isNumeric(numCountString)) {
            numCount = Integer.parseInt(numCountString);
        }
        if (numCount >= 1) {
            String realName = fileName.substring(0, fileName.lastIndexOf("("));
            return fileName = buildName(list, realName + "(" + ++numCount +")", 2);
        }
        return fileName = buildName(list, fileName + "(" + initIndex +")", initIndex++);
    }

    private static boolean decide(String fileName) {
        final String left = "(";
        final String right = ")";
        if (fileName.lastIndexOf(left) < 0) {
            return true;
        }
        if (fileName.lastIndexOf(right) < 0) {
            return true;
        }
        if ((fileName.length() - 1) > fileName.lastIndexOf(right)) {
            return true;
        }
        if ((fileName.lastIndexOf(right) - fileName.lastIndexOf(left)) <= 1) {
            return true;
        }
        return false;
    }

}
