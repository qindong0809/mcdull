package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.PostConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.PostVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IPostRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IPostService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
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
        Page<PostEntity> entityPage = postRepository.selectPage(dto);
        List<PostVO> voList = new ArrayList<>();
        for (PostEntity entity : entityPage.getRecords()) {
            voList.add(PostConvert.convertToVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Transactional(readOnly = true)
    @Override
    public Result<PostVO> detail(Long id) {
        PostEntity postDO = postRepository.getById(id);
        PostVO vo = PostConvert.convertToVO(postDO);
        return Result.success(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(PostLiteDTO dto) {
        boolean isOk = postRepository.checkBusinessUnique(dto.getId(), dto.getPostName());
        if (!isOk) {
            return Result.error(CodeEnum.DATA_EXIST);
        }
        PostEntity postDO = PostConvert.convertToDO(dto);
        postRepository.save(postDO);
        return Result.success(postDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(PostLiteDTO dto) {
        boolean isOk = postRepository.checkBusinessUnique(dto.getId(), dto.getPostName());
        if (!isOk) {
            return Result.error(CodeEnum.DATA_EXIST);
        }
        PostEntity postDO = PostConvert.convertToDO(dto);
        postRepository.updateById(postDO);
        return Result.success(postDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> logicDelete(Long[] ids) {
        List<PostEntity> doList = new ArrayList<>();
        for (Long id : ids) {
            PostEntity postDO = new PostEntity();
            postDO.setId(id);
            doList.add(postDO);
        }
        postRepository.updateBatchById(doList);
        return Result.success();
    }
}
