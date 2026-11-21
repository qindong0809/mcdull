package io.gitee.dqcer.mcdull.common.excel;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.json.util.TimeZoneUtil;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 数据导出（公共逻辑，不依赖任何数据库或服务）。
 * <p>
 * 由业务服务自行分页查询出 VO 列表后传入，本工具只负责把 VO 按字段映射写成表格。
 * 原 system 侧 {@code CommonApiDef.exportExcel(D, Function, ...)} 的回调分页参数无法跨服务序列化，
 * 拆分后改为「调用方查数据 + 本工具写文件」。
 */
public class ExcelExportUtil {

    private ExcelExportUtil() {
    }

    public static <V extends VO> void write(OutputStream outputStream,
                                            List<V> list,
                                            String sheetName,
                                            String exportBy,
                                            String exportTime,
                                            List<Pair<String, Func1<V, ?>>> titleFuncList) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (Pair<String, Func1<V, ?>> entry : titleFuncList) {
            pairList.add(Pair.of(entry.getKey(), LambdaUtil.getFieldName(entry.getValue())));
        }
        List<Map<String, String>> mapList = new ArrayList<>();
        for (V vo : list) {
            Map<String, String> hashMap = new HashMap<>();
            for (Pair<String, String> entry : pairList) {
                Object fieldValue = ReflectUtil.getFieldValue(vo, entry.getValue());
                if (fieldValue instanceof Date date) {
                    fieldValue = TimeZoneUtil.serializeDate(date, null, null, null, null, false);
                }
                if (fieldValue instanceof Boolean) {
                    fieldValue = BooleanUtil.toString(Convert.toBool(fieldValue), "是", "否");
                }
                hashMap.put(entry.getValue(), Convert.toStr(fieldValue));
            }
            mapList.add(hashMap);
        }
        ExcelWriterUtil.exportExcelByMap(outputStream, sheetName, "", exportBy, exportTime, pairList, mapList);
    }
}
