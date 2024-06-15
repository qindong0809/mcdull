package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;


public interface IFormRepository extends IService<FormEntity> {


    Page<FormEntity> selectPage(FormQueryDTO dto);

    FormEntity getByName(String name);
}