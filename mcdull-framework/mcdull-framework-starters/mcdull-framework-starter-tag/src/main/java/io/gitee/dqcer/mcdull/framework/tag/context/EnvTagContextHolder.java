package io.gitee.dqcer.mcdull.framework.tag.context;

import com.alibaba.ttl.TransmittableThreadLocal;

public class EnvTagContextHolder {

    public static final String HEADER_ENV_TAG = "X-Env-Tag";
    public static final String META_ENV_TAG = "env-tag";
    public static final String DEFAULT_TAG = "default";

    private static final TransmittableThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    public static void setTag(String tag) {
        CONTEXT.set(tag);
    }

    public static String getTag() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
