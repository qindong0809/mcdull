package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IDictTransformerService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 字典 业务层
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class DictServiceImpl implements  IDictTransformerService {

    private static final Logger log = LoggerFactory.getLogger(DictServiceImpl.class);

    @Resource
    private IDictRepository dictRepository;

    /**
     * 翻译
     *
     * @param code       代码
     * @param selectType 选择类型
     * @param language   语言
     * @return {@link KeyValueVO}
     */
    @Override
    public KeyValueVO<String, String> transformer(String code, String selectType, String language) {
        LambdaQueryWrapper<DictDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictDO::getCode, code);
        wrapper.eq(DictDO::getSelectType, selectType);
        wrapper.eq(DictDO::getLanguage, language);
        wrapper.eq(DictDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        wrapper.last(GlobalConstant.Database.SQL_LIMIT_1);
        DictDO dictDO = dictRepository.getOne(wrapper);
        if (dictDO == null) {
            log.error("查无数据 code: {}, selectType: {}, language: {}", code, selectType, language);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return new KeyValueVO<String, String>().setId(dictDO.getCode()).setName(dictDO.getName());
    }


}
