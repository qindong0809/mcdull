package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.AreaVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IAreaRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IAreaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Area Service
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class AreaServiceImpl
        extends BasicServiceImpl<IAreaRepository> implements IAreaService {

    @Resource
    private ICommonManager commonManager;

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

    @Override
    public List<LabelValueVO<String, String>> provinceList() {
        return this.buildList(baseRepository.getByAreaType(1));
    }

    @Override
    public List<LabelValueVO<String, String>> cityList(String provinceCode) {
        AreaEntity areaEntity = baseRepository.getCode(provinceCode);
        if (ObjUtil.isNull(areaEntity)) {
            return Collections.emptyList();
        }
        return this.buildList(baseRepository.getByPid(areaEntity.getId()));
    }

    @Override
    public boolean exportData(AreaQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<AreaVO, ?>>> getTitleList() {
        return ListUtil.of(
                Pair.of("区域编码", AreaVO::getCode),
                Pair.of("地名简称", AreaVO::getName),
                Pair.of("全名", AreaVO::getFullname),
                Pair.of("邮政编码", AreaVO::getGovcode),
                Pair.of("维度", AreaVO::getLat),
                Pair.of("经度", AreaVO::getLng)
        );
    }

    private List<LabelValueVO<String, String>> buildList(List<AreaEntity> list) {
        List<LabelValueVO<String, String>> voList = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (AreaEntity entity : list) {
                LabelValueVO<String, String> labelValueVO =
                        new LabelValueVO<>(entity.getCode(), entity.getFullname());
                voList.add(labelValueVO);
            }
        }
        return voList;
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
