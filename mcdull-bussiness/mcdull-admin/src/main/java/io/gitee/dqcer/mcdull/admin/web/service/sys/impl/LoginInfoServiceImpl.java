package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.LoginInfoConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LoginInfoVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserLoginRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILoginInfoService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class LoginInfoServiceImpl implements ILoginInfoService {

    @Resource
    private IUserLoginRepository userLoginRepository;


    @Override
    public Result<PagedVO<LoginInfoVO>> listByPage(LoginInfoLiteDTO dto) {
        List<LoginInfoVO> voList = new ArrayList<>();
        Page<UserLoginDO> entityPage = userLoginRepository.selectPage(dto);

        for (UserLoginDO entity : entityPage.getRecords()) {
            voList.add(LoginInfoConvert.convertToLoginInfoVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
