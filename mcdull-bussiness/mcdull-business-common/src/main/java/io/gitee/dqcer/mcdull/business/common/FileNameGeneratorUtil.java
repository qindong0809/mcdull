package io.gitee.dqcer.mcdull.business.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
public final class FileNameGeneratorUtil {
    private static final String EXCEL = ".xlsx";

    public static String simple(String name) {
        String formatted = DateUtil.formatDateTime(UserContextHolder.getSession().getNow());
        final String format = "%s-%s" + EXCEL;
        return String.format(format, name, formatted);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("文件夹(1)(1)");
        list.add("文件夹(1)(2)");
        list.add("文件夹(3)");
        list.add("文件夹(2)");
        list.add("文件夹(1)");
        list.add("文件夹");

        String name = "文件夹";
        String s = windowsFileName(list, name);
        System.out.println(s);
    }

    public static String windowsFileName(List<String> oldFileNameList, String newTempFileName) {
        Set<String> set = new HashSet<>(oldFileNameList);
        if (set.size() != oldFileNameList.size()) {
            throw new IllegalArgumentException();
        }

        if (!oldFileNameList.contains(newTempFileName)) {
            return newTempFileName;
        }
        oldFileNameList.add(newTempFileName);
        List<String> s = new ArrayList<>();
        Map<String, Integer> nameMap = new HashMap<>(8);
        for (String param : oldFileNameList){
            nameMap.put(param, 0);
        }
        for (String name : oldFileNameList){
            int count = nameMap.get(name);
            String finalName = "";
            if (nameMap.containsKey(name)){
                if (count > 0){
                    finalName = name + "(" + count + ")";
                    if (nameMap.containsKey(finalName)){
                        count++;
                        finalName = name + "(" + count + ")";
                    }
                }else {
                    finalName = name;
                }
            }else {
                finalName = name;
            }
            count++;
            nameMap.put(name, count);
            s.add(finalName);
        }
        String fileName = s.stream().filter(i -> !set.contains(i)).findFirst().orElse(null);
        if (fileName == null) {
            updateFileNameSuffix(s);
            fileName = s.stream().filter(i -> !set.contains(i)).findFirst().orElse(null);
            if (fileName != null){
                return fileName;
            }
            return newTempFileName;
        }
        return fileName;
    }

    private static void updateFileNameSuffix(List<String> s) {
        Map<String, Integer> countMap = s.stream().collect(Collectors.toMap(k -> k, value -> 1, Integer::sum));
        List<String> existKeyList = countMap.keySet().stream().filter(a -> countMap.get(a) > 1).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(existKeyList)) {
            String  existKey = existKeyList.get(0);
            String numCountString =  existKey.substring( existKey.lastIndexOf("(") + 1,  existKey.lastIndexOf(")"));
            if (NumberUtil.isNumber(numCountString)) {
                Integer integer = Convert.toInt(numCountString);
                String fileNamePrefix =  existKey.substring(0,  existKey.lastIndexOf("("));
                s.remove(existKey);
                s.add(fileNamePrefix + "(" + (integer + 1) + ")");
                updateFileNameSuffix(s);
            }
        }
    }

}
