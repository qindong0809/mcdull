package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.system.facade.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.facade.service.SerialNumberApi;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流水号生成实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class SerialNumberApiDefImpl implements SerialNumberApi {

    @Resource
    private ISerialNumberService serialNumberService;

    @Override
    public List<String> generate(SerialNumberGenerateDTO dto) {
        return serialNumberService.generate(dto);
    }
}
