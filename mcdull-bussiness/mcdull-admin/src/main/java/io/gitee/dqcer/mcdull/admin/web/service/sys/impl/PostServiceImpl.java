package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.PostConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.PostVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IPostRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IPostService;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 岗位服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class PostServiceImpl implements IPostService {

    @Resource
    private IPostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public Result<PagedVO<PostVO>> pagedQuery(PostLiteDTO dto) {
        Page<PostDO> entityPage = postRepository.selectPage(dto);
        List<PostVO> voList = new ArrayList<>();
        for (PostDO entity : entityPage.getRecords()) {
            voList.add(PostConvert.convertToVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @Transactional(readOnly = true)
    @Override
    public Result<PostVO> detail(Long id) {
        PostDO postDO = postRepository.getById(id);
        PostVO vo = PostConvert.convertToVO(postDO);
        return Result.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(PostLiteDTO dto) {
        boolean isOk = postRepository.checkBusinessUnique(dto.getId(), dto.getPostName());
        if (!isOk) {
            return Result.error(CodeEnum.DATA_EXIST);
        }
        PostDO postDO = PostConvert.convertToDO(dto);
        postRepository.save(postDO);
        return Result.ok(postDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(PostLiteDTO dto) {
        boolean isOk = postRepository.checkBusinessUnique(dto.getId(), dto.getPostName());
        if (!isOk) {
            return Result.error(CodeEnum.DATA_EXIST);
        }
        PostDO postDO = PostConvert.convertToDO(dto);
        postRepository.updateById(postDO);
        return Result.ok(postDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> logicDelete(Long[] ids) {
        Long userId = UserContextHolder.currentUserId();
        List<PostDO> doList = new ArrayList<>();
        for (Long id : ids) {
            PostDO postDO = new PostDO();
            postDO.setId(id);
            postDO.setDelFlag(DelFlayEnum.DELETED.getCode());
            postDO.setDelBy(userId);
            doList.add(postDO);
        }
        postRepository.updateBatchById(doList);
        return Result.ok();
    }
}