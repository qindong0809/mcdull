package io.gitee.dqcer.mcdull.uac.provider.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import io.gitee.dqcer.mcdull.uac.provider.config.CustomStyleStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel有用
 *
 * @author dqcer
 * @since 2024/06/18
 */
public class ExcelUtil {


    public static void main(String[] args) {
        List<Map<String, String> > data = ListUtil.of(
                MapUtil.of("name", "张三", true),
                MapUtil.of("name", "李四", true));
        exportExcel("D:/22.xlsx", data);
    }

    private static List<List<String>> dynamicHeader(String[] head) {
        return Arrays.stream(head)
                .map(item -> Lists.newArrayList(item.split(",")))
                .collect(Collectors.toList());
    }


    public static void exportExcel(String fileName, List<Map<String, String>> data) {
        // 创建写入器
        EasyExcel.write(fileName).head(dynamicHeader(new String[]{"name"}))
                .registerWriteHandler(new CustomStyleStrategy()).sheet("index").doWrite(data);

    }
}
