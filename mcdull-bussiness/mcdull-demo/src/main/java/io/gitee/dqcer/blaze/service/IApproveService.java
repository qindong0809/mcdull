package io.gitee.dqcer.blaze.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.blaze.domain.entity.Approve;
import io.gitee.dqcer.blaze.domain.vo.ApproveVO;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;

import java.util.List;


/**
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

public interface IApproveService {

    <T extends Approve> void approve(ApproveDTO dto, IRepository<T> baseRepository);

    void setApproveVO(List< ? extends ApproveVO> list, List<? extends Approve> entityList);
}
