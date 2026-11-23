package io.gitee.dqcer.mcdull.common.area;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.IAreaVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 区域名称回填（公共逻辑）。
 * <p>
 * 区域编码到名称的映射由 system 服务远程返回，本工具只负责把映射写回 VO。
 */
public class AreaFillUtil {

    private AreaFillUtil() {
    }

    public static Set<String> collectCodes(Collection<? extends IAreaVO> voList) {
        Set<String> codes = new HashSet<>();
        if (CollUtil.isEmpty(voList)) {
            return codes;
        }
        for (IAreaVO vo : voList) {
            if (ObjUtil.isNotNull(vo.getProvincesCode())) {
                codes.add(vo.getProvincesCode());
            }
            if (ObjUtil.isNotNull(vo.getCityCode())) {
                codes.add(vo.getCityCode());
            }
        }
        return codes;
    }

    public static void fill(Collection<? extends IAreaVO> voList, Map<String, String> nameMap) {
        if (CollUtil.isEmpty(voList) || nameMap == null) {
            return;
        }
        Map<String, String> map = new HashMap<>(nameMap);
        for (IAreaVO vo : voList) {
            if (ObjUtil.isNotNull(vo.getProvincesCode())) {
                vo.setProvincesName(map.get(vo.getProvincesCode()));
            }
            if (ObjUtil.isNotNull(vo.getCityCode())) {
                vo.setCityName(map.get(vo.getCityCode()));
            }
        }
    }
}
