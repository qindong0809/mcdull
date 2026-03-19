package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RemoteDictTypeVO;

import java.util.List;

/**
 * dict value service
 *
 * @author dqcer
 * @since 2024/04/28
 */
public interface IDictValueService extends IRepository<DictValueEntity> {

    /**
     * 获取列表
     *
     * @param dto dto
     * @return {@link PagedVO}<{@link DictValueVO}>
     */
    PagedVO<DictValueVO> getList(DictValueQueryDTO dto);

    /**
     * 插入
     *
     * @param dto dto
     */
    void insert(DictValueAddDTO dto);

    /**
     * update
     *
     * @param dto dto
     */
    void update(DictValueUpdateDTO dto);

    /**
     * delete
     *
     * @param idList id列表
     */
    void delete(List<Integer> idList);

    /**
     * key code
     *
     * @param keyCode keyCode
     * @return {@link List}<{@link DictValueVO}>
     */
    List<DictValueVO> selectByKeyCode(String keyCode);

    /**
     * 字典视图对象
     *
     * @param selectTypeEnum 选择类型
     * @param code       代码
     * @return {@link RemoteDictTypeVO}
     */
    KeyValueBO<String, String> dictVO(IEnum<String> selectTypeEnum, String code);


    void clean();
}
