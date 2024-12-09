package io.gitee.dqcer.mcdull.mdc.provider.web.controller.excl;

import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.*;

/**
 * excel 模板多级级联下拉框示例
 *
 * @author dqcer
 * @since 2024/12/03
 */
public class DemoMain {

    public static void main(String[] args) {
        List<?> dataList = new ArrayList<>();

        /// 准备省市aqu测试数据
        List<String> provinceList = Arrays.asList("apro1", "apro2", "apro3");

        Map<String, List<String>> cityMap = new HashMap<>();
        cityMap.put("apro1", Arrays.asList("acity11", "acity12"));
        cityMap.put("apro2", Arrays.asList("acity21", "acity22"));
        cityMap.put("apro3", Arrays.asList("acity31", "acity32"));

        Map<String, List<String>> districtMap = new HashMap<>();
        districtMap.put("acity11", Arrays.asList("aqu111", "aqu112"));
        districtMap.put("acity12", Arrays.asList("aqu121", "aqu122"));
        districtMap.put("acity21", Arrays.asList("aqu211", "aq212"));
        districtMap.put("acity22", Arrays.asList("aqu221", "aqu222"));
        districtMap.put("acity31", Arrays.asList("aqu311", "aqu312"));
        districtMap.put("acity32", Arrays.asList("aqu321", "aqu322"));


        // 创建写入的Sheet
        File file = new File("D:\\data\\test.xlsx");
        EasyExcel.write(file)
                .sheet("sheet1")
                .registerWriteHandler(new CascadeWriteHandler3(provinceList, cityMap, districtMap))
                .doWrite(dataList);

    }
}
