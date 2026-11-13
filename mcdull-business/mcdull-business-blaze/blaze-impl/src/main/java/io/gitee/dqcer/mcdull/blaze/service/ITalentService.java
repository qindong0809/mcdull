package io.gitee.dqcer.mcdull.blaze.service;

import io.gitee.dqcer.mcdull.blaze.domain.form.TalentAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentVO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;


/**
 * 人才表 业务接口类
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */

public interface ITalentService {

    void insert(TalentAddDTO dto);

    void update(TalentUpdateDTO dto);

    TalentVO detail(Integer id);

    void delete(Integer id);


    PagedVO<TalentVO> queryPage(TalentQueryDTO dto);

    List<LabelValueVO<Integer, String>> list();

    boolean exportData(TalentQueryDTO dto);
}
