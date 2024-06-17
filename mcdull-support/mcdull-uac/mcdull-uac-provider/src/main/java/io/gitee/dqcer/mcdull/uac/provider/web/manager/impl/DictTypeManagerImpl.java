package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.uac.provider.config.constants.CacheConstants;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RemoteDictTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IDictTypeManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  码表通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/24
 */
@Service
public class DictTypeManagerImpl implements IDictTypeManager {

    private static final Logger log = LoggerFactory.getLogger(DictTypeManagerImpl.class);


    @Resource
    private IDictValueService dictValueService;

    @Resource
    private CacheChannel cacheChannel;


    /**
     * 字典视图对象
     *
     * @param selectType 选择类型
     * @param code       代码
     * @return {@link RemoteDictTypeVO}
     */
    @Override
    public KeyValueBO<String, String> dictVO(String selectType, String code){
        if (StrUtil.isBlank(selectType) || StrUtil.isBlank(code)) {
            log.error("select: {} code: {}", selectType, code);
            throw new IllegalArgumentException("参数异常");
        }
        Map<String, String> map = this.getMap(selectType);
        if (CollUtil.isNotEmpty(map)) {
            return new KeyValueBO<>(code, map.get(code));
        }
        return null;
    }

    @Override
    public Map<String, String> codeNameMap(IEnum<String> selectTypeEnum) {
        if (ObjectUtil.isNull(selectTypeEnum)) {
            throw new IllegalArgumentException("'codeList' or 'selectType' is null.");
        }
        List<DictValueVO> list = dictValueService.selectByKeyCode(selectTypeEnum.getCode());
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(DictValueVO::getValueCode, DictValueVO::getValueName));
        }
        return Collections.emptyMap();
    }

    private Map<String, String> getMap(String selectCode) {
        if (ObjectUtil.isNull(selectCode)) {
            throw new IllegalArgumentException("'selectCode' is null.");
        }
        String key = MessageFormat.format(CacheConstants.DICT_LIST,  selectCode);
        List<KeyValueBO<String, String>> list = cacheChannel.get(key, List.class);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(KeyValueBO::getKey, KeyValueBO::getValue));
        }

        List<DictValueVO> dbList = dictValueService.selectByKeyCode(selectCode);
        if (CollUtil.isNotEmpty(dbList)) {
            List<KeyValueBO<String, String>> cacheList = new ArrayList<>();
            for (DictValueVO dictValueVO : dbList) {
                cacheList.add(new KeyValueBO<>(dictValueVO.getValueCode(), dictValueVO.getValueName()));
            }
            cacheChannel.put(key, cacheList, CacheConstants.DICT_EXPIRE);
            return dbList.stream().collect(Collectors.toMap(DictValueVO::getValueCode, DictValueVO::getValueName));
        }
        return Collections.emptyMap();
    }
}
