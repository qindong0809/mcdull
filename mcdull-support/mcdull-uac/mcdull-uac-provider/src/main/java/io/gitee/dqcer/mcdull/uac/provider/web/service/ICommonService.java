package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;

public interface ICommonService {

    String getFileName(FileExtensionTypeEnum fileExtension, String... args);
}
