package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.impl;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.service.DictClientService;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.uac.provider.config.constants.CacheConstants;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RemoteDictVO;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.IDictManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 *  码表通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/24
 */
@Service
public class DictManagerImpl implements IDictManager {

    private static final Logger log = LoggerFactory.getLogger(DictManagerImpl.class);

    @Resource
    private DictClientService dictClientService;

    @Resource
    private CacheChannel cacheChannel;


    /**
     * 字典视图对象
     *
     * @param selectType 选择类型
     * @param code       代码
     * @return {@link RemoteDictVO}
     */
    @Override
    public RemoteDictVO dictVO(String selectType, String code){
        if (StrUtil.isBlank(selectType) || StrUtil.isBlank(code)) {
            log.error("select: {} code: {}", selectType, code);
            throw new IllegalArgumentException("参数异常");
        }
        String language = UserContextHolder.getSession().getLanguage();
        String key = MessageFormat.format(CacheConstants.DICT_ONE, language, selectType, code);
        RemoteDictVO remoteDictVO = cacheChannel.get(key, RemoteDictVO.class);
        if (remoteDictVO != null) {
            return remoteDictVO;
        }
        DictClientDTO dto = new DictClientDTO();
        dto.setCode(code);
        dto.setSelectType(selectType);
        dto.setLanguage(language);
        if (log.isDebugEnabled()) {
            log.debug("查询字典数据请求参数: {}", dto);
        }
        DictClientVO dictVO = ResultParse.getInstance(dictClientService.one(dto));
        RemoteDictVO vo = new RemoteDictVO();
        vo.setCode(dictVO.getCode());
        vo.setName(dictVO.getName());
        vo.setNameShort(dictVO.getNameShort());
        vo.setParentCode(dictVO.getParentCode());
        vo.setSort(dictVO.getSort());
        vo.setDefaulted(dictVO.getDefaulted());
        vo.setStatus(dictVO.getStatus());
        vo.setRemark(dictVO.getRemark());
        vo.setSelectType(dictVO.getSelectType());
        vo.setLanguage(dictVO.getLanguage());
        vo.setDelFlag(dictVO.getDelFlag());

        cacheChannel.put(key, vo, CacheConstants.DICT_EXPIRE);
        return vo;
    }
}
