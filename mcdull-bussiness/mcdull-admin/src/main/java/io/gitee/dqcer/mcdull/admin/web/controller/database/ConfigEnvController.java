package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvTypeDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.ConfigEnvVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.IConfigEnvService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
*  控制器
*
* @author dqcer
* @since 2023-08-29
*/
@RestController
@RequestMapping("/database-config-env")
public class ConfigEnvController {

    @Resource
    private IConfigEnvService configEnvService;


    /**
    * 通过主键查询单条数据
    *
    * @param dto dto
    * @return {@link Result<ConfigEnvVO>}
    */
    @GetMapping("detail")
    public Result<ConfigEnvVO> detail(@Validated ConfigEnvTypeDTO dto){
        return configEnvService.detail(dto.getType());
    }

    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("database:config_env:update")
    @PutMapping("update")
    public Result<Long> update(@RequestBody @Validated ConfigEnvLiteDTO dto){
        return configEnvService.update(dto);
    }

}
