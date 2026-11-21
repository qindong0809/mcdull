package io.gitee.dqcer.mcdull.system.facade.service.def;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件能力契约（仅本地直调，已废弃）
 * <p>
 * 文件操作天然不适合 Feign 远程调用——大文件二进制流跨服务传递成本过高。
 * 本契约仅提供本地实现，微服务模式下文件操作由各业务模块自主处理。
 * <p>
 * 自 blaze 拆分微服务后，文件能力已改为 {@code FileRemoteApi}（multipart + 业务表名），
 * 本契约仅保留给尚未迁移的本地调用方，禁止新增引用。
 */
@Deprecated
public interface FileApiDef {

    /**
     * 按业务实体类批量上传文件
     */
    void batchFileUpload(List<MultipartFile> fileList, Integer bizId, Class<?> clazz, List<Integer> deleteFileIdList);

    /**
     * 删除业务关联文件
     */
    void remove(Integer bizId, Class<?> clazz);

}
