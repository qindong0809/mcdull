package io.gitee.dqcer.mcdull.blaze.dao.repository;

import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentQueryDTO;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 人才表 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */
public interface ITalentRepository extends IService<TalentEntity> {


    List<TalentEntity> queryListByIds(List<Integer> idList);

    Page<TalentEntity> selectPage(TalentQueryDTO dto);

}
