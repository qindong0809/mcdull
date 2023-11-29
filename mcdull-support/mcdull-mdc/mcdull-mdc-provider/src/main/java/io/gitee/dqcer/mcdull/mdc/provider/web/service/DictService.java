package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.DictConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
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
public class DictService {

    private static final Logger log = LoggerFactory.getLogger(DictService.class);

    @Resource
    private IDictRepository dictRepository;

    public List<DictClientVO> list(String selectType) {
        List<DictClientVO> list = new ArrayList<>();
        if (StrUtil.isNotBlank(selectType)) {
            if (log.isInfoEnabled()) {
                log.info("list. selectType: {}", selectType);
            }
            List<DictDO> dbList = dictRepository.list(selectType);
            if (CollUtil.isNotEmpty(dbList)) {
                list = DictConvert.entitiesConvertToList(dbList);
            }
        }
        return list;
    }
}
