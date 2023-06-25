package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IUserTransformerService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IPostRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IUserService;
import io.gitee.dqcer.mcdull.business.common.FileNameGeneratorUtil;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户服务 impl
 *
 * @author dqcer
 * @since 2023/01/15 13:01:30
 */
@Service
public class UserServiceImpl extends BasicServiceImpl<IUserRepository> implements IUserService, IUserTransformerService {

    @Resource
    private IUserManager userManager;

    @Resource
    private IRoleRepository repository;

    @Resource
    private IPostRepository postRepository;

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result < PagedVO < UserVO >>}
     */
    @Override
    public Result<PagedVO<UserVO>> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = baseRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        List<UserDO> list = baseRepository.list();
        Map<Long, UserDO> userIdMap = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        for (UserDO entity : entityPage.getRecords()) {
            UserVO vo = userManager.entityToVo(entity);
//            vo.setCreatedByStr(userIdMap.get(vo.getCreatedBy()).getNickName());
            voList.add(vo);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 单个详情
     *
     * @param userId userId
     * @return {@link Result<UserVO>}
     */
    @Override
    public Result<UserDetailVO> detail(Long userId) {
        UserDetailVO vo = new UserDetailVO();
        if (ObjUtil.isNotNull(userId)) {
            UserDO userDO = baseRepository.getById(userId);
            vo = userManager.entityToDetailVo(userDO);
            return Result.ok(vo);
        }
        List<RoleDO> roleList = repository.getAll();

        List<BaseVO<Long, String>> roleBaseList = new ArrayList<>();
        for (RoleDO roleDO : roleList) {
            if (!UserContextHolder.isAdmin()) {
//                if ("2".equals(roleDO.getType())) {
//                    continue;
//                }
            }
            BaseVO<Long, String> baseVO = new BaseVO<>();
            baseVO.setId(roleDO.getId());
            baseVO.setName(roleDO.getName());
            roleBaseList.add(baseVO);
        }
        vo.setRoles(roleBaseList);

        List<PostDO> postList = postRepository.getAll();
        List<BaseVO<Long, String>> postBaseList = new ArrayList<>();
        for (PostDO postDO : postList) {
            BaseVO<Long, String> baseVO = new BaseVO<>();
            baseVO.setId(postDO.getId());
            baseVO.setName(postDO.getPostName());
            postBaseList.add(baseVO);
        }
        vo.setPosts(postBaseList);
        return Result.ok(vo);
    }


    @Override
    public Result<Long> insertOrUpdate(UserInsertDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getAccount, dto.getAccount());
        query.ne(ObjUtil.isNotNull(dto.getId()), IdDO::getId, dto.getId());
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserDO> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }

        if (ObjUtil.isNull(dto.getId())) {
            UserDO entity = UserConvert.dtoToEntity(dto);

            String salt = RandomUtil.uuid();
            String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
            entity.setSalt(salt);
            entity.setPassword(password);
            entity.setType(UserTypeEnum.READ_WRITE.getCode());
            entity.setStatus(StatusEnum.ENABLE.getCode());
            Long userId = baseRepository.insert(entity);

//        userRoleRepository.updateByUserId(userId, dto.getRoleIds());

            return Result.ok(userId);
        }

        UserDO userDO = baseRepository.getById(dto.getId());
        userDO.setDeptId(dto.getDeptId());
        userDO.setPhone(dto.getPhone());
        userDO.setEmail(dto.getEmail());
        userDO.setNickName(dto.getNickName());
        userDO.setAccount(dto.getAccount());
        userDO.setStatus(dto.getStatus());
        baseRepository.updateById(userDO);
        return Result.ok(dto.getId());
    }

    /**
     * 更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> update(UserLiteDTO dto) {
        return null;
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> updateStatus(StatusDTO dto) {
        return null;
    }

    @Override
    public Result<Long> delete(Long id) {
        this.validDeleteParam(id);
        baseRepository.delete(id);
        return Result.ok(id);
    }

    private void validDeleteParam(Long id) {
        Long currentUserId = UserContextHolder.currentUserId();
        if (id.equals(currentUserId)) {
            throw new BusinessException("不能对自己的账号进行删除");
        }
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> resetPassword(UserLiteDTO dto) {
        return null;
    }

    @SneakyThrows
    @Override
    public Result<Boolean> export(UserLiteDTO dto) {
        dto.setNotNeedPaged(true);
        Result<PagedVO<UserVO>> pagedVOResult = this.listByPage(dto);
        List<UserVO> list = ResultParse.getPageData(pagedVOResult, Result::getData);

        HttpServletResponse response = ServletUtil.getResponse();
        String fileName = FileNameGeneratorUtil.simple("用户信息");
        ServletUtil.setDownloadExcelHttpHeader(response, fileName);
        EasyExcel.write(response.getOutputStream(),  UserVO.class).sheet().doWrite(list);
        return Result.ok(true);
    }

    /**
     * 翻译
     *
     * @param code  编码
     * @return {@link KeyValueVO}
     */
    @Override
    public KeyValueBO<String, String> transformer(String code) {
        UserDO userDO = baseRepository.getById(code);
        if (ObjUtil.isNotNull(userDO)) {
            return new KeyValueBO<String, String>().setKey(userDO.getId().toString()).setValue(userDO.getNickName());
        }
        return new KeyValueBO<>();
    }
}
