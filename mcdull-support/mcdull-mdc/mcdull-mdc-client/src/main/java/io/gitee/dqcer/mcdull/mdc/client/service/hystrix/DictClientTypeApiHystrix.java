package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.service.DictTypeApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class DictClientTypeApiHystrix implements DictTypeApi {

    @Override
    public InnerResult<DictTypeClientVO> one(DictClientDTO dto) {
        return InnerResult.error("hystrix fall back!");
    }

    @Override
    public InnerResult<List<DictTypeClientVO>> list(String selectType) {
        return InnerResult.error("hystrix fall back!");
    }
}
