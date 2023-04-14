package io.gitee.dqcer.mcdull.framework.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * basic service impl
 *
 * @author dqcer
 * @since 2023/04/14
 */
@SuppressWarnings("all")
public class BasicServiceImpl<R extends IService> {

    protected  Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected R baseRepository;

}
