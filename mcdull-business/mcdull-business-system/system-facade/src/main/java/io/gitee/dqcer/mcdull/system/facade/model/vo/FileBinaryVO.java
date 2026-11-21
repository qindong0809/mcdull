package io.gitee.dqcer.mcdull.system.facade.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 文件二进制内容（远程下载用，Jackson 自动转 base64）
 */
@Data
public class FileBinaryVO implements VO {

    private String fileName;

    private byte[] content;
}
