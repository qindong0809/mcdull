package io.gitee.dqcer.mcdull.framework.web.basic;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * basic service impl
 *
 * @author dqcer
 * @since 2023/04/14
 */
@SuppressWarnings("all")
public abstract class BasicServiceImpl<R extends IRepository> extends GenericLogic {

    @Autowired
    protected R baseRepository;

    protected <T> T mustGet(Serializable id, Class<T> clazz) {
        Object obj = baseRepository.getById(id);
        if (ObjUtil.isNull(obj)) {
            this.throwDataNotExistException(id);
        }
        if (obj instanceof RelEntity<?>) {
            RelEntity entity = (RelEntity) obj;
            if (BooleanUtil.isTrue(entity.getDelFlag())) {
                this.throwDataNotExistException(id);
            }
        }
        return clazz.cast(obj);
    }
}
