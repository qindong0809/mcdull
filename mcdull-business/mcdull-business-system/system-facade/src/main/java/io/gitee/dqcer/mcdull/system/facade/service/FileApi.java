package io.gitee.dqcer.mcdull.system.facade.service;

import io.gitee.dqcer.mcdull.system.facade.service.def.FileApiDef;

/**
 * 文件能力对外接口
 * <p>
 * 文件操作涉及 MultipartFile 二进制流、{@code Class<?>} 业务实体类型等无法跨服务序列化的参数，
 * 且本地实现（{@code FileApiDefImpl}）未暴露 HTTP 端点，因此本接口仅用于单体合并部署下的本地直调，
 * 不声明 {@code @RpcClient}。若未来需要跨服务文件能力，应另设计以业务类型字符串为入参的查询/上传契约。
 *
 * @author dqcer
 * @since 2026-11-20
 */
public interface FileApi extends FileApiDef {
}
