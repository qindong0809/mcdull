package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.DeptMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDeptRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 部门 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class DeptRepositoryImpl extends ServiceImpl<DeptMapper, DeptDO>  implements IDeptRepository {


    @Override
    public List<DeptDO> list(String name, Integer status) {
        LambdaQueryWrapper<DeptDO> wrapper = Wrappers.lambdaQuery();
        if (ObjUtil.isNotEmpty(status)) {
            wrapper.eq(DeptDO::getStatus, status);
        }
        if (StrUtil.isNotBlank(name)) {
            wrapper.like(DeptDO::getName, name);
        }
        List<DeptDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }
}