package io.gitee.dqcer.mcdull.workflow.web.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowDefinitionDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowDefinitionVO;
import io.gitee.dqcer.mcdull.workflow.web.service.IFlowDefinitionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.warm.flow.core.enums.PublishStatus;
import org.dromara.warm.flow.orm.entity.FlowDefinition;
import org.dromara.warm.flow.orm.mapper.FlowDefinitionMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FlowDefinitionServiceImpl implements IFlowDefinitionService {

    @Resource
    private FlowDefinitionMapper flowDefinitionMapper;

    @Override
    public PagedVO<FlowDefinitionVO> getList(FlowDefinitionDTO dto) {
        LambdaQueryWrapper<FlowDefinition> wrapper = buildQueryWrapper(dto);
        wrapper.eq(FlowDefinition::getIsPublish, PublishStatus.PUBLISHED.getKey());
        Page<FlowDefinition> page = flowDefinitionMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), wrapper);
        if (ObjUtil.isNotNull(page.getTotal())) {
            List<FlowDefinitionVO> voList = new ArrayList<>();
            for (FlowDefinition record : page.getRecords()) {
                FlowDefinitionVO vo = this.getFlowDefinitionVO(record);
                voList.add(vo);
            }
            return PageUtil.toPage(voList, page);
        }
        return PageUtil.empty(dto);
    }

    private FlowDefinitionVO getFlowDefinitionVO(FlowDefinition record) {
        FlowDefinitionVO vo = new FlowDefinitionVO();
        vo.setId(record.getId());
        vo.setCreateTime(record.getCreateTime());
        vo.setUpdateTime(record.getUpdateTime());
        vo.setTenantId(record.getTenantId());
        vo.setDelFlag(record.getDelFlag());
        vo.setFlowCode(record.getFlowCode());
        vo.setFlowName(record.getFlowName());
        vo.setCategory(record.getCategory());
        vo.setVersion(record.getVersion());
        vo.setIsPublish(record.getIsPublish());
        vo.setFormCustom(record.getFormCustom());
        vo.setFormPath(record.getFormPath());
        vo.setActivityStatus(record.getActivityStatus());
        vo.setListenerType(record.getListenerType());
        vo.setListenerPath(record.getListenerPath());
        vo.setExt(record.getExt());
        return vo;
    }

    private LambdaQueryWrapper<FlowDefinition> buildQueryWrapper(FlowDefinition flowDefinition) {
        LambdaQueryWrapper<FlowDefinition> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjUtil.isNotNull(flowDefinition.getId()), FlowDefinition::getId, flowDefinition.getId());
        wrapper.eq(StrUtil.isNotBlank(flowDefinition.getCategory()), FlowDefinition::getCategory, flowDefinition.getCategory());
        wrapper.like(StrUtil.isNotBlank(flowDefinition.getFlowCode()), FlowDefinition::getFlowCode, flowDefinition.getFlowCode());
        wrapper.like(StrUtil.isNotBlank(flowDefinition.getFlowName()), FlowDefinition::getFlowName, flowDefinition.getFlowName());
        wrapper.eq(StrUtil.isNotBlank(flowDefinition.getVersion()), FlowDefinition::getVersion, flowDefinition.getVersion());
        wrapper.eq(ObjUtil.isNotNull(flowDefinition.getIsPublish()), FlowDefinition::getIsPublish, flowDefinition.getIsPublish());
        wrapper.eq(ObjUtil.isNotNull(flowDefinition.getActivityStatus()), FlowDefinition::getActivityStatus, flowDefinition.getActivityStatus());
        wrapper.eq(StrUtil.isNotBlank(flowDefinition.getListenerType()), FlowDefinition::getListenerType, flowDefinition.getListenerType());
        wrapper.orderByDesc(FlowDefinition::getCreateTime);
        return wrapper;
    }
}
