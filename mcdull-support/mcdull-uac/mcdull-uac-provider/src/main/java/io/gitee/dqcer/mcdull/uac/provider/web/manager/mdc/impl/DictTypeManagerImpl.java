package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.framework.feign.ResultApiParse;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.service.DictTypeApi;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.uac.provider.config.constants.CacheConstants;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RemoteDictTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.IDictTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private DictTypeApi dictClientService;

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
    public RemoteDictTypeVO dictVO(String selectType, String code){
        if (StrUtil.isBlank(selectType) || StrUtil.isBlank(code)) {
            log.error("select: {} code: {}", selectType, code);
            throw new IllegalArgumentException("参数异常");
        }
        String language = UserContextHolder.getSession().getLanguage();
        String key = MessageFormat.format(CacheConstants.DICT_ONE, language, selectType, code);
        RemoteDictTypeVO remoteDictTypeVO = cacheChannel.get(key, RemoteDictTypeVO.class);
        if (remoteDictTypeVO != null) {
            return remoteDictTypeVO;
        }
        DictClientDTO dto = new DictClientDTO();
        dto.setCode(code);
        dto.setSelectType(selectType);
        dto.setLanguage(language);
        if (log.isDebugEnabled()) {
            log.debug("查询字典数据请求参数: {}", dto);
        }
        DictTypeClientVO dictVO = ResultApiParse.getInstance(dictClientService.one(dto));
        RemoteDictTypeVO vo = this.convertRemoteDictTypeVO(dictVO);
        cacheChannel.put(key, vo, CacheConstants.DICT_EXPIRE);
        return vo;
    }

    private RemoteDictTypeVO convertRemoteDictTypeVO(DictTypeClientVO dictVO) {
        RemoteDictTypeVO remoteDictTypeVO = new RemoteDictTypeVO();
        remoteDictTypeVO.setId(dictVO.getId());
        remoteDictTypeVO.setDictName(dictVO.getDictName());
        remoteDictTypeVO.setDictType(dictVO.getDictType());
        remoteDictTypeVO.setRemark(dictVO.getRemark());
        remoteDictTypeVO.setInactive(dictVO.getInactive());
        return remoteDictTypeVO;
    }

    @Override
    public Map<String, String> codeNameMap(IEnum<String> selectTypeEnum) {
        if (ObjectUtil.isNull(selectTypeEnum)) {
            throw new IllegalArgumentException("'codeList' or 'selectType' is null.");
        }
        ResultApi<List<DictTypeClientVO>> result = dictClientService.list(selectTypeEnum.getCode());
        List<DictTypeClientVO> list = ResultApiParse.getInstance(result);
        if (CollUtil.isNotEmpty(list)) {
//            return list.stream().collect(Collectors.toMap(DictTypeClientVO::getCode, DictTypeClientVO::getName));
        }
        return Collections.emptyMap();
    }
}
