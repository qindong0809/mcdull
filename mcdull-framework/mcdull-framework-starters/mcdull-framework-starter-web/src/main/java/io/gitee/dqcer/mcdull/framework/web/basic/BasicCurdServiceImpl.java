package io.gitee.dqcer.mcdull.framework.web.basic;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * basic curd service impl
 *
 * @author dqcer
 * @since 2026/02/09
 */
public abstract class BasicCurdServiceImpl<M extends BaseMapper<T>, T> extends CrudRepository<M, T> {

    @Resource
    protected DynamicLocaleMessageSource dynamicLocaleMessageSource;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected T mustGet(Serializable id) {
        T obj = super.getById(id);
        if (ObjectUtil.isNull(obj)) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        return obj;
    }

}
