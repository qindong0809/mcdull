package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.DictTypeConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictTypeRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictTypeService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层
 *
 * @author dqcer
 * @since  2023/03/18
 */
@Service
public class DictTypeServiceImpl implements IDictTypeService {

    @Resource
    private IDictTypeRepository dictTypeRepository;

    @Override
    public Result<PagedVO<DictTypeVO>> list(DictTypeLiteDTO dto) {
        List<DictTypeVO> voList = new ArrayList<>();

        Page<DictTypeDO> entityPage = dictTypeRepository.selectPage(dto);
        for (DictTypeDO entity : entityPage.getRecords()) {
            voList.add(DictTypeConvert.convertToDictTypeVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<DictTypeVO> detail(Long dictId) {
        DictTypeDO repository = dictTypeRepository.getById(dictId);
        return Result.ok(DictTypeConvert.convertToDictTypeVO(repository));
    }

    @Override
    public Result<List<DictTypeVO>> getAll() {
        List<DictTypeVO> voList = new ArrayList<>();

        List<DictTypeDO> list = dictTypeRepository.list();
        for (DictTypeDO dictTypeDO : list) {
            voList.add(DictTypeConvert.convertToDictTypeVO(dictTypeDO));
        }
        return Result.ok(voList);
    }
}
