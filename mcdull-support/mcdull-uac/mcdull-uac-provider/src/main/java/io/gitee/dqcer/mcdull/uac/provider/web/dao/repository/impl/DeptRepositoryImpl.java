package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DeptDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.DeptMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDeptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class DeptRepositoryImpl extends ServiceImpl<DeptMapper, DeptDO> implements IDeptRepository {



    @Override
    public Integer insert(DeptDO entity) {
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }


    @Override
    public boolean delete(Integer id, String reason) {
        return removeById(id);
    }

    @Override
    public List<DeptDO> listByParentId(Integer parentId) {
        LambdaQueryWrapper<DeptDO> query = Wrappers.lambdaQuery();
        query.eq(DeptDO::getParentId, parentId);
        return baseMapper.selectList(query);
    }
}
