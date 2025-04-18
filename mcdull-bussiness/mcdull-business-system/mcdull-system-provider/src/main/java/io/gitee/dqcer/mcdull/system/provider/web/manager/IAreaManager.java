package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.system.provider.model.vo.IArea;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface IAreaManager {

    Map<String, String> map(Set<String> codeSet);

    <T extends IArea> void set(List<T> list);
}
