package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
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
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 登录日志 服务
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
        Page<UserLoginDO> entityPage = userLoginRepository.paged(dto);

        for (UserLoginDO entity : entityPage.getRecords()) {
            voList.add(LoginInfoConvert.convertToLoginInfoVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @SneakyThrows(IOException.class)
    @Override
    public void exportExcel(LoginInfoLiteDTO dto) {
        dto.setNotNeedPaged(true);

        Result<PagedVO<LoginInfoVO>> result = this.listByPage(dto);
        List<LoginInfoVO> voList = result.getData().getList();

        String prefix = this.builderFileNamePrefix(dto.getModuleMenuName());
        HttpServletResponse response = ServletUtil.getResponse();
        ServletUtil.setDownloadExcelHttpHeader(response, prefix);

        EasyExcel.write(response.getOutputStream(), LoginInfoVO.class).sheet().doWrite(voList);
    }

    private String builderFileNamePrefix(String name) {
        String dateTime = DateUtil.formatDateTime(new Date());
        return MessageFormat.format("{0} {1}", name, dateTime);
    }

}
