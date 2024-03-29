package io.gitee.dqcer.mcdull.admin.web.dao.repository.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigEntity;

import java.util.List;

/**
 * 系统配置 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface ISysConfigRepository extends IService<SysConfigEntity> {

    /**
     * 根据key查找
     *
     * @param key 关键
     * @return {@link String}
     */
    SysConfigEntity findByKey(String key);

    Page<SysConfigEntity> selectPage(ConfigLiteDTO dto);

    List<SysConfigEntity> getListByKey(String configKey);

    void removeUpdate(Long id);
}
