package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.config.constants.CacheConstants;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RemoteDictTypeVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IDictTypeManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictValueService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private RedissonCache redissonCache;


    /**
     * 字典视图对象
     *
     * @param selectTypeEnum 选择类型
     * @param code       代码
     * @return {@link RemoteDictTypeVO}
     */
    @Override
    public KeyValueBO<String, String> dictVO(IEnum<String> selectTypeEnum, String code){
        if (ObjectUtil.isNull(selectTypeEnum) || StrUtil.isBlank(code)) {
            log.error("select: {} code: {}", selectTypeEnum, code);
            throw new IllegalArgumentException("参数异常");
        }
        Map<String, String> map = this.getMap(selectTypeEnum.getCode());
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

    @Override
    public void clean() {
        redissonCache.evict(CacheConstants.DICT_LIST);
    }

    private Map<String, String> getMap(String selectCode) {
        if (ObjectUtil.isNull(selectCode)) {
            throw new IllegalArgumentException("'selectCode' is null.");
        }
        String key = StrUtil.format(CacheConstants.DICT_LIST,  selectCode);
        List<DictValueVO> dbList = redissonCache.getListOrSet(key, DictValueVO.class, () -> dictValueService.selectByKeyCode(selectCode), CacheConstants.DICT_EXPIRE);
        if (CollUtil.isNotEmpty(dbList)) {
            return dbList.stream().collect(Collectors.toMap(DictValueVO::getValueCode, DictValueVO::getValueName, (k1, k2) -> k1));
        }
        return Collections.emptyMap();
    }
}
