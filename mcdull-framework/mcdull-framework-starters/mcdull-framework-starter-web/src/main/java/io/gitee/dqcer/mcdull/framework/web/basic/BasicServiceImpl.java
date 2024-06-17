package io.gitee.dqcer.mcdull.framework.web.basic;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * basic service impl
 *
 * @author dqcer
 * @since 2023/04/14
 */
@SuppressWarnings("all")
public abstract class BasicServiceImpl<R extends IService> extends GenericLogic {

    @Autowired
    protected R baseRepository;

    protected Object checkDataExistById(Serializable id) {
        Object obj = baseRepository.getById(id);
        if (ObjUtil.isNull(obj)) {
            this.throwDataNotExistException(id);
        }
        return obj;
    }

}
