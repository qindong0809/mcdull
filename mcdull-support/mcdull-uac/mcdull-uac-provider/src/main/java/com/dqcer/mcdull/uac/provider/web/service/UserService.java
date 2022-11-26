package com.dqcer.mcdull.uac.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.page.PageUtil;
import com.dqcer.framework.base.page.Paged;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.api.convert.UserConvert;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.SysUserEntity;
import com.dqcer.mcdull.uac.api.vo.RemoteDictVO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.config.constants.DictSelectTypeEnum;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import com.dqcer.mcdull.uac.provider.web.manager.mdc.IDictManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Resource
    private IUserRepository userRepository;
    
    @Resource
    private IDictManager dictManager;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    public Result<Paged<UserVO>> listByPage(UserLiteDTO dto) {
        Page<SysUserEntity> entityPage = userRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        for (SysUserEntity entity : entityPage.getRecords()) {
            // TODO: 2022/11/25
//            dictClientService.one()
            UserVO vo = UserConvert.entityToVO(entity);
//            RemoteDictVO dictVO = dictManager.dictVO(DictSelectTypeEnum.STATUS.getCode(), entity.getStatus().toString());
//            vo.setStatus();
            voList.add(vo);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 详情
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    public Result<UserVO> detail(UserLiteDTO dto) {
        return Result.ok(UserConvert.entityToVO(userRepository.getById(dto.getId())));
    }
}
