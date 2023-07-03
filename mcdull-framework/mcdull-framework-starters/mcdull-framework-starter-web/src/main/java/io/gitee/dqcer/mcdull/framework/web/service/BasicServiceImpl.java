package io.gitee.dqcer.mcdull.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * basic service impl
 *
 * @author dqcer
 * @since 2023/04/14
 */
@SuppressWarnings("all")
public abstract class BasicServiceImpl<R extends IService> {

    protected  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected R baseRepository;

    protected void validNameExist(Serializable id, String name) {
    }

    protected void throwDataExistException() {
        throw new BusinessException(CodeEnum.DATA_EXIST);
    }

}
