package io.gitee.dqcer.mcdull.framework.tag.context;

import io.gitee.dqcer.mcdull.framework.spi.AsyncContextPropagator;
import org.springframework.util.StringUtils;

/**
 * 环境染色标签的跨线程传播器。
 * <p>
 * 空串与 {@code null} 都按「无标签」处理，与 {@link EnvTagContextHolder#setTag(String)} 的调用方保持一致，
 * 否则一个空标签会被当成有效快照传给执行线程。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public class EnvTagContextPropagator implements AsyncContextPropagator {

    @Override
    public Object capture() {
        String tag = EnvTagContextHolder.getTag();
        return StringUtils.hasText(tag) ? tag : null;
    }

    @Override
    public void apply(Object snapshot) {
        if (snapshot instanceof String tag && StringUtils.hasText(tag)) {
            EnvTagContextHolder.setTag(tag);
        } else {
            EnvTagContextHolder.clear();
        }
    }
}
