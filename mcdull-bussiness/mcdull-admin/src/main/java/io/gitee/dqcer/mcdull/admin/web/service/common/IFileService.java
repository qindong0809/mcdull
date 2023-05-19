package io.gitee.dqcer.mcdull.admin.web.service.common;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.multipart.MultipartFile;

/**
* file interface
*
* @author dqcer
* @since 2023-01-19
*/
public interface IFileService {


    Result<Boolean> save(MultipartFile multipartFile);


}
