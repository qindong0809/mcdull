package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.database.GitConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GitVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGitRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGitService;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance 服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class GitServiceImpl extends BasicServiceImpl<IGitRepository> implements IGitService {


    @Override
    public Result<PagedVO<GitVO>> list(GitListDTO dto) {
        List<GitVO> voList = new ArrayList<>();
        Page<GitDO> entityPage = baseRepository.selectPage(dto);
        for (GitDO entity : entityPage.getRecords()) {
            voList.add(GitConvert.convertToGitVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<GitVO> detail(Long id) {
        GitDO entity = baseRepository.getById(id);
        return Result.ok(GitConvert.convertToGitVO(entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(GitAddDTO dto) {
        List<GitDO> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(null, dto.getName(), list);
        GitDO sysConfigDO = GitConvert.convertToGitDo(dto);
        sysConfigDO.setDelFlag(DelFlayEnum.NORMAL.getCode());
        baseRepository.save(sysConfigDO);
        return Result.ok(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(GitEditDTO dto) {
        List<GitDO> list = baseRepository.getListByName(dto.getName());
        this.validNameExist(dto.getId(), dto.getName(), list);
        GitDO entity = GitConvert.convertToGitDo(dto);
        entity.setId(dto.getId());
        baseRepository.updateById(entity);
        return Result.ok(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.ok(id);
    }

    @Override
    public Result<List<GitVO>> allList() {
        List<GitVO> voList = new ArrayList<>();
        List<GitDO> list = baseRepository.allList();
        if (CollUtil.isNotEmpty(list)) {
            for (GitDO GitDO : list) {
                GitVO vo = GitConvert.convertToGitVO(GitDO);
                voList.add(vo);
            }
        }
        return Result.ok(voList);
    }
}
