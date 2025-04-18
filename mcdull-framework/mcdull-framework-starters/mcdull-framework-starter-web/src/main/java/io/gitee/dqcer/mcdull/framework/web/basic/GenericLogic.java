package io.gitee.dqcer.mcdull.framework.web.basic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * basic service impl
 *
 * @author dqcer
 * @since 2023/04/14
 */
@SuppressWarnings("all")
public abstract class GenericLogic {

    @Resource
    protected DynamicLocaleMessageSource dynamicLocaleMessageSource;

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected <T extends IdEntity<?>> void validNameExist(Serializable id,
                                                          String name,
                                                          List<T> list, Predicate<T> uniquenessPredicate) {
        if (id == null) {
            if (CollUtil.isNotEmpty(list)) {
                long count = list.stream().filter(uniquenessPredicate).count();
                if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
                    this.throwDataExistException(name);
                }
            }
            return;
        }
        long count = list.stream().filter(uniquenessPredicate).count();
        if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
            this.throwDataExistException(name);
        }
    }

    /**
     * 数据存在关联
     *
     * @param data 数据
     */
    protected void throwDataExistAssociated(Object data) {
        LogHelp.error(log, "Data associated need disassociate. data:{}", data);
        throw new BusinessException(I18nConstants.DATA_ASSOCIATED_NEED_DISASSOCIATE);
    }



    protected void throwDataNotExistException(Object data) {
        LogHelp.error(log, "Data not exists.  data: {}", () -> data);
        throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
    }

    protected void throwDataExistException(String name) {
        LogHelp.error(log, "Data exists.  data: {}", () -> name);
        throw new BusinessException(I18nConstants.DATA_EXISTS);
    }

    protected void throwDataNotExistException(Serializable id) {
        LogHelp.error(log, "Data not exists.  data: {}", () -> id);
        throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
    }

    protected void throwMissingParmeterException(String format, Object... objects) {
        LogHelp.error(log, "Missing parameter. " + format, objects);
        throw new BusinessException(I18nConstants.MISSING_PARAMETER);
    }

    protected void throwDataNeedRefreshException(String format, Object... objects) {
        LogHelp.error(log, "Data need refresh. " + format, objects);
        throw new BusinessException(I18nConstants.DATA_NEED_REFRESH);
    }

    protected void throwSystemBusyException(String format, Object... objects) {
        LogHelp.error(log, "System busy. " + format, objects);
        throw new BusinessException(I18nConstants.SYSTEM_BUSY);
    }

}
