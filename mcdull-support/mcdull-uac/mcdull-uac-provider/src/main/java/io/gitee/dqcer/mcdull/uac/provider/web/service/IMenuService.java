package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MenuVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RouterVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IMenuService {
    Map<Integer, List<String>> getMenuCodeListMap(List<Integer> roleIdList);

    List<String> getAllCodeList();

    List<RouterVO> allTree();

    List<RouterVO> tree(Integer userId);

    List<RouterVO> treeByRoleId(Integer roleId);

    List<MenuVO> list(MenuListDTO dto);

    boolean insert(MenuInsertDTO dto);

    boolean update(Integer id, MenuUpdateDTO dto);

    boolean delete(Integer id, ReasonDTO dto);
}
