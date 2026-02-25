package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.SerialNumberVO;

import java.util.List;

/**
 * Serial Number Service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISerialNumberService extends IRepository<SerialNumberEntity> {

    /**
     * all
     *
     * @return {@link List }<{@link SerialNumberVO }>
     */
    List<SerialNumberVO> getAll();

    /**
     * generate
     *
     * @param dto DTO
     * @return {@link List }<{@link String }>
     */
    List<String> generate(SerialNumberGenerateDTO dto);
}
