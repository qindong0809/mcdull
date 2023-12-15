package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DeptLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.CurrentUserInfVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ExportDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.util.ExportUtil;
import io.gitee.dqcer.mcdull.admin.web.service.sso.IAuthService;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDeptService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* 部门 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@Slf4j
@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;

    @Resource
    private IAuthService authService;

    @Resource
    private RedisTemplate redisTemplate;


    /**
    * 列表查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Transform
    @Authorized("system:dept:list")
    @GetMapping("list")
    public Result<List<DeptVO>> list(@Validated(value = {ValidGroup.List.class}) DeptLiteDTO dto){
        return deptService.list(dto);
    }

    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门id
     * @return {@link Result}<{@link List}<{@link DeptVO}>>
     */
    @GetMapping("list/exclude/{deptId}")
    public Result<List<DeptVO>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId){
        return deptService.excludeChild(deptId);
    }


    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门id
     * @return {@link Result}<{@link List}<{@link DeptVO}>>
     */
    @GetMapping("{deptId}")
    public Result<DeptVO> detail(@PathVariable(value = "deptId", required = false) Long deptId){
        return deptService.detail(deptId);
    }

    @GetMapping("exportExcel")
    public void exportExcel(HttpServletResponse response,DeptLiteDTO dto) throws IOException{
        //1、设定响应类型
        response.setContentType("application/vnd.ms-excel");
        // 设置防止中文乱码
        response.setCharacterEncoding("UTF-8");
        //2、设定附件的打开方法为：下载，并指定文件名称为category.xlsx
        String fileName = "dept_export_" + new Date().getTime() + ".xlsx";
        response.setHeader("content-disposition","attachment;filename=" + fileName);

        // String path = "F:\\" + fileName;
        //3、创建工作簿
        ExcelWriter writeWork = EasyExcel.write(response.getOutputStream()).build();
        // 写多张表
        WriteSheet writeSheet1 = EasyExcel.writerSheet(1,"index").head(ExportDetailVO.class).build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(2,"部门信息").head(DeptVO.class).build();
        //4、创建表格
        // ExcelWriterSheetBuilder sheet = writeWork.sheet("wokendaybaby");
        //5、调用业务层获取数据
        Result<List<DeptVO>> exportDataToExcel = deptService.list(dto);
        Result<CurrentUserInfVO> currentUserInfo = authService.getCurrentUserInfo();
        String nickName = currentUserInfo.getData().getUser().getNickName();

        String status = dto.getStatus();
        String deptName = dto.getDeptName();
        StringBuilder sb = new StringBuilder();
        if(status != null && status.length() != 0){
            sb.append("部门状态").append(";");
        }
        if(deptName != null && deptName.length()!= 0){
            sb.append("部门名称").append(";");
        }
        String filterCondition = sb.toString();
        ExportDetailVO exportDetailVO = new ExportDetailVO();
        exportDetailVO.setExportUser(nickName);
        exportDetailVO.setExportDate(new Date());
        exportDetailVO.setFilterCondition(filterCondition);
        List<ExportDetailVO> list = new ArrayList<>();
        list.add(exportDetailVO);
        //6、写入数据到表格中
        writeWork.write(list,writeSheet1);
        writeWork.write(exportDataToExcel.getData(),writeSheet2);
        // 多张表写入必须调用finish（），会帮助关闭流
        writeWork.finish();
        response.flushBuffer();
    }

    @GetMapping("exportExcel1")
    public void exportExcel1(HttpServletResponse response,DeptLiteDTO dto) throws IOException{
        String fileName = "dept_export_" + new Date().getTime() + ".xlsx";

        // Result<List<DeptVO>> exportDataToExcel = deptService.list(dto);
        Result<List<DeptVO>> exportDataToExcel = listTree(dto);
        List<DeptVO> data = exportDataToExcel.getData();
        Result<CurrentUserInfVO> currentUserInfo = authService.getCurrentUserInfo();
        String nickName = currentUserInfo.getData().getUser().getNickName();

        String status = dto.getStatus();
        String deptName = dto.getDeptName();
        StringBuilder sb = new StringBuilder();
        if(status != null && status.length() != 0){
            sb.append("部门状态").append(";");
        }
        if(deptName != null && deptName.length()!= 0){
            sb.append("部门名称").append(";");
        }
        String filterCondition = sb.toString();
        ExportDetailVO exportDetailVO = new ExportDetailVO();
        exportDetailVO.setExportUser(nickName);
        exportDetailVO.setExportDate(new Date());
        exportDetailVO.setFilterCondition(filterCondition);
        List<ExportDetailVO> list = new ArrayList<>();
        list.add(exportDetailVO);

        // 从这里开始准备调用公共导出方法的参数
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"index");
        map.put(2,"部门信息");
        Map<Integer,List<?>> dataMap = new HashMap<>();
        dataMap.put(1,list);
        dataMap.put(2,data);

        ExportUtil.commonExport(response,fileName,map,dataMap);
    }

    @PostMapping("add")
    public Result<String> addDept(@RequestBody @Validated({ValidGroup.List.class}) DeptVO deptVO){
        UserVO user = authService.getCurrentUserInfo().getData().getUser();
        Long id = user.getId();
        String lockValue = "lock_" + id;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", lockValue,300, TimeUnit.SECONDS);
        if (lock) {
            //2.4释放锁，del
            // 判断比较uuid值是否一样
            String lockUUid = (String) redisTemplate.opsForValue().get("lock");
            Result<String> result = deptService.add(deptVO);
            if (lockUUid.equals(lockValue)) {
                // redisTemplate.delete("lock");
            }
            return result;

        } else {
            //3获取锁失败、每隔0.1秒再获取
            try {
                Thread.sleep(100);
                addDept(deptVO);
                return Result.error("查询速度太快，稍后再试");
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.error("出bug了");
            }
        }

    }

    @PutMapping()
    public Result<String> updateDept(@RequestBody @Validated({ValidGroup.List.class}) DeptVO deptVO){

        return deptService.update(deptVO);
    }

    @GetMapping("list/tree")
    public Result<List<DeptVO>> listTree(DeptLiteDTO dto){
        Result<List<TreeSelectVO>> listResult = deptService.selectDeptTreeList();
        Result<List<DeptVO>> list = deptService.list(dto);
        List<DeptVO> deptVOList = list.getData();
        Map<Long,DeptVO> map = new HashMap<>();
        for (DeptVO deptVO : deptVOList) {
            Long deptId = deptVO.getDeptId();
            if(deptVO.getStatus().equals("1")){
                deptVO.setStatus("正常");
            }else if(deptVO.getStatus().equals("2")){
                deptVO.setStatus("停用");
            }
            map.put(deptId,deptVO);
        }
        ArrayList<DeptVO> deptTreeVO = new ArrayList<>();
        List<Long> deptId = new ArrayList<>();
        List<TreeSelectVO> data = listResult.getData();
        forTreeSelectVO(data,deptId);
        for (Long aLong : deptId) {
            DeptVO deptVO = map.get(aLong);
            deptTreeVO.add(deptVO);
        }

        return Result.success(deptTreeVO);
    }


    /**
     * 遍历树形成一个按树形排列的部门id的list
     * @param list
     * @param sourceList
     */
    public void forTreeSelectVO(List<TreeSelectVO> list,List<Long> sourceList){

        if(list != null && list.size() != 0){

            for (TreeSelectVO treeSelectVO : list) {
                Long id = treeSelectVO.getId();
                sourceList.add(id);
                List<TreeSelectVO> children = treeSelectVO.getChildren();
                forTreeSelectVO(children,sourceList);
            }
        }

    }




}
