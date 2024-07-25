package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;

/**
 * Common service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface ICommonService {

    String getFileName(FileExtensionTypeEnum fileExtension, String... args);
}
