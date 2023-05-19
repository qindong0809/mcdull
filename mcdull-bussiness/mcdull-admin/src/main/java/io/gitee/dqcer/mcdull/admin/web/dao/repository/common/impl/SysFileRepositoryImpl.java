package io.gitee.dqcer.mcdull.admin.web.dao.repository.common.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysFileDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.common.SysFileMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysFileRepository;
import org.springframework.stereotype.Service;

/**
 * sys file 数据库操作封装实现层
 *
 * @author dqcer
 * @since  2022/12/25
 */
@Service
public class SysFileRepositoryImpl extends ServiceImpl<SysFileMapper, SysFileDO> implements ISysFileRepository {

}
