package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

@Data
public class FileSimpleVO implements VO {

    private Integer id;

    private String fileName;

    private String url;
}
