package io.gitee.dqcer.mcdull.system.provider.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantUserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface TenantUserMapper extends BaseMapper<TenantUserEntity> {

    @Select("SELECT tenant_id FROM sys_tenant_user WHERE user_id = #{userId} AND inactive = 0 AND del_flag = 0")
    Set<Integer> selectTenantIdsByUserId(@Param("userId") Integer userId);

    @Select("SELECT user_id FROM sys_tenant_user WHERE tenant_id = #{tenantId} AND inactive = 0 AND del_flag = 0")
    Set<Integer> selectUserIdsByTenantId(@Param("tenantId") Integer tenantId);

    @Delete("<script>" +
            "DELETE FROM sys_tenant_user WHERE tenant_id = #{tenantId} AND user_id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    int deleteByTenantAndUsers(@Param("tenantId") Integer tenantId, @Param("userIds") List<Integer> userIds);
}
