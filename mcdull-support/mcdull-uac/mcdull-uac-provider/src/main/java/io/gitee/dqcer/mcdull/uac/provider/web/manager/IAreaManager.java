package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface IAreaManager {

    Map<String, String> map(Set<String> codeSet);
}
