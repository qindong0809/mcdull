package io.gitee.dqcer.mcdull.mdc.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.DictConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictTypeRepository;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.IDictTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DictTypeServiceImpl implements IDictTypeService {

    private static final Logger log = LoggerFactory.getLogger(DictTypeServiceImpl.class);

    @Resource
    private IDictTypeRepository dictRepository;

    @Override
    public List<DictTypeClientVO> list(String selectType) {
        List<DictTypeClientVO> list = new ArrayList<>();
        if (StrUtil.isNotBlank(selectType)) {
            if (log.isInfoEnabled()) {
                log.info("list. selectType: {}", selectType);
            }
            List<DictTypeDO> dbList = dictRepository.list(selectType);
            if (CollUtil.isNotEmpty(dbList)) {
                list = DictConvert.entitiesConvertToList(dbList);
            }
        }
        return list;
    }
}
