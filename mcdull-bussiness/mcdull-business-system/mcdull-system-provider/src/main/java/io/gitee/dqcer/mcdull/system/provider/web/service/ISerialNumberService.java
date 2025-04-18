package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.SerialNumberVO;

import java.util.List;

/**
 * Serial Number Service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISerialNumberService {

    List<SerialNumberVO> getAll();

    List<String> generate(SerialNumberGenerateDTO dto);
}
