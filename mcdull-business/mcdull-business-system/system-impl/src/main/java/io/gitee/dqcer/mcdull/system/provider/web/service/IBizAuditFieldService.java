package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;

import java.util.List;
import java.util.Map;

public interface IBizAuditFieldService extends IRepository<BizAuditFieldEntity> {

    Map<Integer, List<BizAuditFieldEntity>> map(List<Integer> bizAuditIdList);

    List<BizAuditFieldEntity> like(String value);
}
