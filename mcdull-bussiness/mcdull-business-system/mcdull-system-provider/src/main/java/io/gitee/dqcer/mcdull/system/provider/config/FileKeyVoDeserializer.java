package io.gitee.dqcer.mcdull.system.provider.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileVO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 由于前端接收到的是序列化过的字段, 这边入库需要进行反序列化操作比较方便处理
 *
 * @author dqcer
 * @since 2024/06/18
 */
@Slf4j
public class FileKeyVoDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<FileVO> list = new ArrayList<>();
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode listOrObjectNode = objectCodec.readTree(jsonParser);
        String deserialize = "";
        try {
            if (listOrObjectNode.isArray()) {
                for (JsonNode node : listOrObjectNode) {
                    list.add(objectCodec.treeToValue(node, FileVO.class));
                }
            } else {
                list.add(objectCodec.treeToValue(listOrObjectNode, FileVO.class));
            }
            deserialize = list.stream()
                    .map(FileVO::getFileKey)
                    .collect(Collectors.joining(StrUtil.COMMA));
        } catch (Exception e) {
            LogHelp.error(log, "反序列化失败", e);
            deserialize = listOrObjectNode.asText();
        }
        return deserialize;
    }


}
