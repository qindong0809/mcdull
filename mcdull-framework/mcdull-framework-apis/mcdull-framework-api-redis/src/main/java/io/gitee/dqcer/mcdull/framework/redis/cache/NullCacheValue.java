package io.gitee.dqcer.mcdull.framework.redis.cache;

/**
 * 缓存空值哨兵：用于区分「缓存未命中」与「已缓存的空结果」。
 * <p>
 * Redisson 的 {@code RBucket.set(null)} 等价于删除 key，无法直接缓存 null；
 * 因此当回源结果为空时写入本对象，读取时再转换回 null / 空集合。
 * <p>
 * 本类必须是 public 且无参可构造：Redis 序列化使用 Jackson defaultTyping，
 * 其多态类型白名单已放行 {@code io.gitee.dqcer.mcdull.} 包。
 *
 * @author dqcer
 */
public final class NullCacheValue {

    public static final NullCacheValue INSTANCE = new NullCacheValue();

    public NullCacheValue() {
    }
}
