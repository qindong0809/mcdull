package io.gitee.dqcer.mcdull.uac.provider.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件key进行序列化对象
 *
 * @author dqcer
 * @since 2024/06/18
 */
public class FileKeyVoSerializer extends JsonSerializer<String> {

    @Resource
    private IFileService fileService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeObject(new ArrayList<>());
            return;
        }
        if(fileService == null){
            jsonGenerator.writeString(value);
            return;
        }
        String[] fileKeyArray = value.split(StrUtil.COMMA);
        List<String> fileKeyList = Arrays.asList(fileKeyArray);
        List<FileVO> fileKeyVOList = fileService.getFileList(fileKeyList);
        jsonGenerator.writeObject(fileKeyVOList);
    }
}
