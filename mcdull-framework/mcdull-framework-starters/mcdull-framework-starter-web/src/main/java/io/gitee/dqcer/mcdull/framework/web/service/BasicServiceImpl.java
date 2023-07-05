package io.gitee.dqcer.mcdull.framework.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    protected <T extends BaseDO> void validNameExist(Serializable id, String name) {
    }

    protected <T extends BaseDO> void validNameExist(Serializable id, String name, List<T> list) {
        if (id == null) {
            if (CollUtil.isNotEmpty(list)) {
                this.throwDataExistException();
            }
            return;
        }
        long count = list.stream().filter(i -> !Objects.equals(id, i.getId())).count();
        if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
            this.throwDataExistException();
        }
    }

    protected void throwDataExistException() {
        throw new BusinessException(CodeEnum.DATA_EXIST);
    }

}
