package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.DictTypeConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDictTypeRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictTypeService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class DictTypeServiceImpl extends BasicServiceImpl<IDictTypeRepository> implements IDictTypeService {

    @Resource
    private IDictTypeRepository dictTypeRepository;

    @Override
    public Result<PagedVO<DictTypeVO>> list(DictTypeLiteDTO dto) {
        List<DictTypeVO> voList = new ArrayList<>();

        Page<DictTypeEntity> entityPage = dictTypeRepository.selectPage(dto);
        for (DictTypeEntity entity : entityPage.getRecords()) {
            voList.add(DictTypeConvert.convertToDictTypeVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<DictTypeVO> detail(Long dictId) {
        DictTypeEntity repository = dictTypeRepository.getById(dictId);
        return Result.success(DictTypeConvert.convertToDictTypeVO(repository));
    }

    @Override
    public Result<List<DictTypeVO>> getAll() {
        List<DictTypeVO> voList = new ArrayList<>();

        List<DictTypeEntity> list = dictTypeRepository.list();
        for (DictTypeEntity dictTypeDO : list) {
            voList.add(DictTypeConvert.convertToDictTypeVO(dictTypeDO));
        }
        return Result.success(voList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(DictTypeAddDTO dto) {
        List<DictTypeEntity> list = baseRepository.getListByName(dto.getDictType());
        this.validNameExist(null, dto.getDictType(), list);
        DictTypeEntity entity = DictTypeConvert.convertToDictTypeDo(dto);
        baseRepository.save(entity);
        return Result.success(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(DictTypeEditDTO dto) {
        List<DictTypeEntity> list = baseRepository.getListByName(dto.getDictType());
        this.validNameExist(dto.getDictId(), dto.getDictType(), list);
        DictTypeEntity entity = DictTypeConvert.convertToDictTypeDo(dto);
        entity.setId(dto.getDictId());
        baseRepository.updateById(entity);
        return Result.success(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdateById(id);
        return Result.success(id);
    }


}
