package io.gitee.dqcer.mcdull.system.provider.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantMenuEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface TenantMenuMapper extends BaseMapper<TenantMenuEntity> {

    @Select("SELECT menu_id FROM sys_tenant_menu WHERE tenant_id = #{tenantId} AND inactive = 0 AND del_flag = 0")
    Set<Integer> selectMenuIdsByTenantId(@Param("tenantId") Integer tenantId);

    @Delete("DELETE FROM sys_tenant_menu WHERE tenant_id = #{tenantId}")
    int deleteByTenantId(@Param("tenantId") Integer tenantId);

    @Delete("<script>" +
            "DELETE FROM sys_tenant_menu WHERE tenant_id = #{tenantId} AND menu_id IN " +
            "<foreach collection='menuIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    int deleteByTenantAndMenus(@Param("tenantId") Integer tenantId, @Param("menuIds") List<Integer> menuIds);
}
