package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.service.DictApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class DictClientApiHystrix implements DictApi {

    @Override
    public ResultApi<DictClientVO> one(DictClientDTO dto) {
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<List<DictClientVO>> list(String selectType) {
        return ResultApi.error("hystrix fall back!");
    }
}
