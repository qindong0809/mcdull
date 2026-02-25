package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.LoginLogMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginLogService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Login Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class LoginLogServiceImpl
        extends BasicCurdServiceImpl<LoginLogMapper, LoginLogEntity> implements ILoginLogService {

    @Resource
    private IUserService userService;
    @Resource
    private ICommonManager commonManager;

    @Transactional(readOnly = true)
    @Override
    public PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto) {
        Integer userId = dto.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            UserEntity user = userService.get(userId);
            if (ObjUtil.isNotNull(user)) {
                dto.setUserName(user.getLoginName());
            }
        }
        Page<LoginLogEntity> entityPage = this.selectPage(dto);
        List<LoginLogVO> voList = new ArrayList<>();
        List<LoginLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            for (LoginLogEntity entity : records) {
                LoginLogVO vo = this.convertToConfigVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void add(LoginLogEntity entity) {
        String remark = entity.getRemark();
        if (StrUtil.isNotBlank(remark)) {
            entity.setRemark(dynamicLocaleMessageSource.getMessage(remark));
        }
        this.insert(entity);
    }

    @Override
    public LoginLogEntity getFirstLoginLog(String loginName) {
        return this.getFirst(loginName);
    }

    @Override
    public LoginLogEntity getLastLoginLog(String loginName) {
        List<LoginLogEntity> list = this.getListByLoginName(loginName);
        if (CollUtil.isNotEmpty(list)) {
            List<LoginLogEntity> successLoginList = list.stream()
                    .filter(item -> LoginLogResultTypeEnum.LOGIN_SUCCESS.getCode().equals(item.getLoginResult()))
                    .sorted((o1, o2) -> o2.getCreatedTime().compareTo(o1.getCreatedTime())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(successLoginList)) {
                return successLoginList.get(0);
            }
        }
        return null;
    }

    @Override
    public boolean exportData(LoginLogQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryByPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<LoginLogVO, ?>>> getTitleList() {
        return ListUtil.of(
                Pair.of("登录名", LoginLogVO::getLoginName),
                Pair.of("IP", LoginLogVO::getLoginIp),
                Pair.of("IP地区", LoginLogVO::getLoginIpRegion),
                Pair.of("设备信息", LoginLogVO::getUserAgent),
                Pair.of("登录结果", LoginLogVO::getLoginResultName),
                Pair.of("备注", LoginLogVO::getRemark),
                Pair.of("时间", LoginLogVO::getCreateTime)
        );
    }

    private LoginLogVO convertToConfigVO(LoginLogEntity entity) {
        LoginLogVO vo = new LoginLogVO();
        vo.setLoginLogId(entity.getId());
        vo.setUserId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setLoginIp(entity.getLoginIp());
        vo.setLoginIpRegion(entity.getLoginIpRegion());
        vo.setUserAgent(entity.getUserAgent());
        vo.setRemark(entity.getRemark());
        vo.setLoginResult(entity.getLoginResult());
        vo.setLoginResultName(IEnum.getTextByCode(LoginLogResultTypeEnum.class, entity.getLoginResult()));
        vo.setCreateTime(entity.getCreatedTime());
        return vo;
    }


    public List<LoginLogEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(LoginLogEntity::getId, idList);
        return baseMapper.selectList(wrapper);
    }

    public Page<LoginLogEntity> selectPage(LoginLogQueryDTO param) {
        LambdaQueryWrapper<LoginLogEntity> lambda = new QueryWrapper<LoginLogEntity>().lambda();
        lambda.like(ObjectUtil.isNotNull(param.getKeyword()), LoginLogEntity::getLoginName, param.getKeyword());
        lambda.like(ObjectUtil.isNotNull(param.getIp()), LoginLogEntity::getLoginIp, param.getIp());
        lambda.eq(CharSequenceUtil.isNotBlank(param.getUserName()), LoginLogEntity::getLoginName, param.getUserName());
        Date startDate = param.getStartDate();
        Date endDate = param.getEndDate();
        if (ObjectUtil.isNotNull(startDate) && ObjectUtil.isNotNull(endDate)) {
            lambda.between(LoginLogEntity::getCreatedTime, startDate, endDate);
        }
        lambda.orderByDesc(ListUtil.of(TimestampEntity::getCreatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public LoginLogEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    public Integer insert(LoginLogEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public boolean exist(LoginLogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public LoginLogEntity getFirst(String loginName) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LoginLogEntity::getLoginName, loginName);
        wrapper.ne(LoginLogEntity::getLoginResult, LoginLogResultTypeEnum.LOGIN_FAIL);
        wrapper.orderByAsc(LoginLogEntity::getCreatedTime);
        List<LoginLogEntity> list =  baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public List<LoginLogEntity> getListByLoginName(String loginName) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LoginLogEntity::getLoginName, loginName);
        return baseMapper.selectList(wrapper);
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}
