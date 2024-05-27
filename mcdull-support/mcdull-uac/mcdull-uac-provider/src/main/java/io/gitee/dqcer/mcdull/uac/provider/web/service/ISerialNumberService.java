package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SerialNumberVO;

import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface ISerialNumberService {

    List<SerialNumberVO> getAll();

    List<String> generate(SerialNumberGenerateDTO dto);
}
