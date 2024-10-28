package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.BizAuditMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IBizAuditRepository;
import org.springframework.stereotype.Service;


/**
 * biz audit 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class BizAuditRepositoryImpl extends
        ServiceImpl<BizAuditMapper, BizAuditEntity> implements IBizAuditRepository {

}
