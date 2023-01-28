package io.gitee.dqcer.mcdull.frameowrk.mongodb.wrapper;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
