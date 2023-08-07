package io.gitee.dqcer.mcdull.admin.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 阿俊
 * @create 2023-07-19 11:24
 */
@Slf4j
public class ExportUtil {

    /**
     * 这是一个导出的公共方法
     * @param response 浏览器返回响应
     * @param fileName 要输出的文件名称
     * @param map   要输出的表序号和表名称的map集合
     * @param dataMap   要输出的表序号和对应的list数据
     */
    public static void commonExport(HttpServletResponse response,
                                    String fileName,
                                    Map<Integer,String> map,
                                    Map<Integer, List<?>> dataMap){

        ServletUtil.setDownloadExcelHttpHeader(response,fileName);
        ExcelWriter writeWork = null;
        try {
            writeWork = EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            log.error("io");
            throw new RuntimeException("ffffffffffffff");
        }
        for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
            Integer key = integerStringEntry.getKey();
            String value = integerStringEntry.getValue();
            List<?> ts = dataMap.get(key);
            if(ts == null){
                throw new RuntimeException("表序号和要写入的数据不一致");
            }else if(ts.size() == 0){
                throw new RuntimeException("要写入的数据为空");
            }

            Class<?> clazz = ts.get(0).getClass();


            WriteSheet buildSheet = EasyExcel.writerSheet(key, value).head(clazz).build();
            writeWork.write(ts,buildSheet);
        }
        writeWork.finish();
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
