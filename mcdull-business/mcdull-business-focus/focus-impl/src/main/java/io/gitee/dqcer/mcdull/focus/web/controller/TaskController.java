package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskCreateDTO;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskUpdateDTO;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.model.enums.TaskBatchBuildDateEnum;
import io.gitee.dqcer.mcdull.focus.web.mapper.AppTaskMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class TaskController extends BasicController {
    @Resource
    private AppTaskMapper appTaskMapper;

    @Operation(summary = "task list")
    @PostMapping(GlobalConstant.APP_PATH + "/task/list")
    public Result<List<AppTaskEntity>> list(@RequestBody Map<String, Object> param) {
        Object completed = param.get("completed");
        LambdaQueryWrapper<AppTaskEntity> query = Wrappers.lambdaQuery();
        query.eq(ObjectUtil.isNotNull(completed), AppTaskEntity::getCompleted, completed);
        query.eq(AppTaskEntity::getCreatedBy, UserContextHolder.userId());
        query.orderByAsc(AppTaskEntity::getDueDate);
        return Result.success(appTaskMapper.selectList(query));
    }
    public void validReadOnlyUser() {
        Integer i = UserContextHolder.userId();
        if (i == 1) {
            // 游客模式下不能操作，赶快取注册一个账号吧
            throw new BusinessException(CodeEnum.READ_ONLY);
        }
    }


    @Operation(summary = "task create")
    @PostMapping(GlobalConstant.APP_PATH + "/task/create")
    public Result<Map<String, Object>> create(@RequestBody @Valid TaskCreateDTO dto) {
        List<Integer> idList = new ArrayList<>();
        validReadOnlyUser();
        List<Date> dateList = buildDateList(dto.getBatchBuildDate(), dto.getDueDate());
        for (Date date : dateList) {
            AppTaskEntity entity = new AppTaskEntity();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setDueDate(date);
            entity.setPriority(dto.getPriority());
            entity.setCategory(dto.getCategory());
            entity.setCompleted(false);
            entity.setReminderEnabled(dto.getReminderEnabled());
            entity.setCreatedBy(UserContextHolder.userId());
            appTaskMapper.insert(entity);
            idList.add(entity.getId());
        }
        return Result.success(Map.of("ids", idList, "dueDates", dateList));
    }

    private List<Date> buildDateList(Integer batchBuildDate, Date dueDate) {
        List<Date> dateList = new ArrayList<>();
        List<String> hhmm = ListUtil.of("09:45", "11:00", "14:00", "15:15", "16:00", "17:15");
        if (ObjectUtil.equals(batchBuildDate, TaskBatchBuildDateEnum.TODAY.getCode())) {
            for (String s : hhmm) {
                dateList.add(DateUtil.parse(DateUtil.today() + " " + s));
            }
        } else if (ObjectUtil.equals(batchBuildDate, TaskBatchBuildDateEnum.WEEK.getCode())) {
            // 获取本周的开始日期
            Date beginDate = DateUtil.beginOfWeek(new Date());
            for (int i = 0; i < 7; i++) {
                for (String s : hhmm) {
                    dateList.add(DateUtil.parse(DateUtil.formatDate(DateUtil.offsetDay(beginDate, i)) + " " + s));
                }
            }
        } else {
            dateList.add(dueDate);
        }
        return dateList;
    }

    // /task/update — 更新任务
    @Operation(summary = "task update")
    @PostMapping(GlobalConstant.APP_PATH + "/task/update")
    public Result<Map<String, Object>> update(@RequestBody @Valid TaskUpdateDTO dto) {
        validReadOnlyUser();
        AppTaskEntity update = new AppTaskEntity();
        update.setId(dto.getId());
        update.setName(dto.getName());
        update.setDescription(dto.getDescription());
        update.setDueDate(dto.getDueDate());
        update.setPriority(dto.getPriority());
        update.setCategory(dto.getCategory());
        update.setReminderEnabled(dto.getReminderEnabled());
        appTaskMapper.updateById(update);
        return Result.success(Map.of("id", update.getId(), "dueDate", update.getDueDate()));
    }

    @Operation(summary = "task delete")
    @PostMapping(GlobalConstant.APP_PATH + "/task/delete")
    public Result<Integer> delete(@RequestBody Map<String, Object> param) {
        validReadOnlyUser();
        Integer id = (Integer) param.get("id");
        appTaskMapper.deleteById(id);
        return Result.success(id);
    }

    @Operation(summary = "task toggle")
    @PostMapping(GlobalConstant.APP_PATH + "/task/toggle")
    public Result<Integer> toggle(@RequestBody Map<String, Object> param) {
        validReadOnlyUser();
        Integer id = (Integer) param.get("id");
        AppTaskEntity task = appTaskMapper.selectById(id);
        task.setCompleted(!task.getCompleted());
        appTaskMapper.updateById(task);
        return Result.success(task.getId());
    }

}
