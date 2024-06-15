package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.AreaVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IAreaRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IAreaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Area Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Service
public class AreaServiceImpl
        extends BasicServiceImpl<IAreaRepository> implements IAreaService {

    public PagedVO<AreaVO> queryPage(AreaQueryDTO dto) {
        List<AreaVO> voList = new ArrayList<>();
        Page<AreaEntity> entityPage = baseRepository.selectPage(dto);
        List<AreaEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (AreaEntity entity : recordList) {
                AreaVO vo = this.convertToVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private AreaVO convertToVO(AreaEntity item){
        AreaVO vo = new AreaVO();
        vo.setCode(item.getCode());
        vo.setName(item.getName());
        vo.setFullname(item.getFullname());
        vo.setGovcode(item.getGovcode());
        vo.setLat(item.getLat());
        vo.setLng(item.getLng());
        return vo;
    }


}
