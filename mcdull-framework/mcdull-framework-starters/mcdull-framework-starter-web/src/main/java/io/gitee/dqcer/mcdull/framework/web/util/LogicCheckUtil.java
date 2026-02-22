package io.gitee.dqcer.mcdull.framework.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * logic check utility
 *
 * @author dqcer
 * @since 2023/04/14
 */
@Slf4j
public class LogicCheckUtil  {

    public static  <T extends IdEntity<?>> void validNameExist(Serializable id,
                                                          String name,
                                                          List<T> list, Predicate<T> uniquenessPredicate) {
        if (id == null) {
            if (CollUtil.isNotEmpty(list)) {
                long count = list.stream().filter(uniquenessPredicate).count();
                if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
                    throwDataExistException(name);
                }
            }
            return;
        }
        long count = list.stream().filter(uniquenessPredicate).count();
        if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
            throwDataExistException(name);
        }
    }

    /**
     * 数据存在关联
     *
     * @param data 数据
     */
    public static void throwDataExistAssociated(Object data) {
        LogHelp.error(log, "Data associated need disassociate. data:{}", data);
        throw new BusinessException(I18nConstants.DATA_ASSOCIATED_NEED_DISASSOCIATE);
    }



    public static void throwDataNotExistException(Object data) {
        LogHelp.error(log, "Data not exists.  data: {}", () -> data);
        throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
    }

    public static void throwDataExistException(String name) {
        LogHelp.error(log, "Data exists.  data: {}", () -> name);
        throw new BusinessException(I18nConstants.DATA_EXISTS);
    }

    public static void throwDataNotExistException(Serializable id) {
        LogHelp.error(log, "Data not exists.  data: {}", () -> id);
        throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
    }

    public static void throwMissingParmeterException(String format, Object... objects) {
        LogHelp.error(log, "Missing parameter. " + format, objects);
        throw new BusinessException(I18nConstants.MISSING_PARAMETER);
    }

    public static void throwDataNeedRefreshException(String format, Object... objects) {
        LogHelp.error(log, "Data need refresh. " + format, objects);
        throw new BusinessException(I18nConstants.DATA_NEED_REFRESH);
    }

    public static void throwSystemBusyException(String format, Object... objects) {
        LogHelp.error(log, "System busy. " + format, objects);
        throw new BusinessException(I18nConstants.SYSTEM_BUSY);
    }

}
