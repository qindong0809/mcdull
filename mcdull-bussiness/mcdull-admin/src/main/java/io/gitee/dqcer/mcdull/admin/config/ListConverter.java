package io.gitee.dqcer.mcdull.admin.config;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListConverter implements Converter<List> {
    @Override
    public Class supportJavaTypeKey() {
        return List.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }



    @Override
    public List convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        String[] split = stringValue.split(",");
        List<String> enterpriseList = new ArrayList<>();
        for(int i = 0; i < split.length; i++){
            enterpriseList.add(split[i]);
        }
        return enterpriseList;
    }

    @Override
    public WriteCellData<?> convertToExcelData(List list, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(o -> {
            String s = o.toString();
            stringBuilder.append(s+",");
        });
        return new WriteCellData(stringBuilder.toString());
    }

}
