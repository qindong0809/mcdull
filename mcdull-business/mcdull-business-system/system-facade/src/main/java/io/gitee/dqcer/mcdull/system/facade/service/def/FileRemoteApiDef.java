package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.facade.model.dto.FileBizDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileBinaryVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileSimpleVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件能力远程契约（涉及 system 文件表与对象存储）。
 * <p>
 * 用 {@code bizType}（业务表 {@code @TableName} 值）替代原本地契约中不可序列化的 {@code Class<?>}；
 * 上传走 multipart，由 OpenFeign 4.x 自带的 SpringEncoder 处理。
 */
public interface FileRemoteApiDef {

    /**
     * 批量上传并绑定业务
     */
    @PostMapping(value = GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/file/remote/batch-upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void batchUpload(@RequestPart("meta") FileBizDTO meta,
                     @RequestPart(value = "files", required = false) List<MultipartFile> files);

    /**
     * 删除业务关联文件
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/file/remote/remove")
    void remove(@RequestBody FileBizDTO dto);

    /**
     * 批量查询业务文件（用于回填 VO 的 fileList）
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/file/remote/list")
    Map<Integer, List<FileSimpleVO>> listFiles(@RequestBody FileBizDTO dto);

    /**
     * 下载业务文件二进制
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/file/remote/download")
    List<FileBinaryVO> downloadFiles(@RequestBody FileBizDTO dto);

    /**
     * 上传文件到系统文件夹（导出自动归档用）
     */
    @PostMapping(value = GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/file/remote/upload-system",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void uploadToSystemFolder(@RequestPart("file") MultipartFile file);
}
