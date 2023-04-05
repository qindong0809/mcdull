package io.gitee.dqcer.mcdull.admin.model.convert.sys;


import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.PostVO;

/**
 * 岗位 对象转换工具类
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class PostConvert {



    public static PostVO convertToVO(PostDO entity) {
        PostVO postVO = new PostVO();
        postVO.setId(entity.getId());
        postVO.setPostCode(entity.getPostCode());
        postVO.setPostName(entity.getPostName());
        postVO.setPostSort(entity.getPostSort());
        postVO.setStatus(entity.getStatus());
        postVO.setRemark(entity.getRemark());
        postVO.setCreatedTime(entity.getCreatedTime());
        postVO.setCreatedBy(entity.getCreatedBy());
        postVO.setUpdatedTime(entity.getUpdatedTime());
        postVO.setUpdatedBy(entity.getUpdatedBy());
        postVO.setDelFlag(entity.getDelFlag());
        return postVO;
    }

    public static PostDO convertToDO(PostLiteDTO dto) {
        PostDO postDO = new PostDO();
        postDO.setPostCode(dto.getPostCode());
        postDO.setPostName(dto.getPostName());
        postDO.setPostSort(dto.getPostSort());
        postDO.setStatus(dto.getStatus());
        postDO.setRemark(dto.getRemark());
        postDO.setId(dto.getId());
        return postDO;
    }
}
