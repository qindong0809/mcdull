package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import io.gitee.dqcer.mcdull.admin.config.LocalDateTimeConverter;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DeptLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDeptService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* 部门 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;


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
        ExcelWriterBuilder writeWork = EasyExcel.write(response.getOutputStream() , DeptVO.class);
        //4、创建表格
        ExcelWriterSheetBuilder sheet = writeWork.sheet("wokendaybaby");
        //5、调用业务层获取数据
        Result<List<DeptVO>> exportDataToExcel = deptService.list(dto);
        //6、写入数据到表格中
        sheet.doWrite(exportDataToExcel.getData());
    }





}
