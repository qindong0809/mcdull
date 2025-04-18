package io.gitee.dqcer.mcdull.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.blaze.domain.entity.CustomerInfoEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.CustomerInfoQueryDTO;

import java.util.List;


/**
 * 企业信息 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */
public interface ICustomerInfoRepository extends IService<CustomerInfoEntity> {


    List<CustomerInfoEntity> queryListByIds(List<Integer> idList);

    Page<CustomerInfoEntity> selectPage(CustomerInfoQueryDTO dto);

}
