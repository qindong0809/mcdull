package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICommonService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommonServiceImpl implements ICommonService {

    @Override
    public String getFileName(FileExtensionTypeEnum fileExtension, String... args) {
        if (ObjUtil.isNull(fileExtension) || ArrayUtil.isEmpty(args)) {
            throw new IllegalArgumentException();
        }
        StrJoiner joiner = new StrJoiner(StrUtil.UNDERLINE);
        joiner.append(args);
        Date now = UserContextHolder.getSession().getNow();
        String dateStr = TimeZoneUtil.serializeDate(now, DatePattern.PURE_DATETIME_PATTERN);
        return StrUtil.format("{}_{}{}", joiner, dateStr, fileExtension.getCode());
    }
}
