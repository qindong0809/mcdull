package io.gitee.dqcer.mcdull.demo.facade;

import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskPriorityEnum;
import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskStatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 契约枚举自测，同时验证 @EnumsIntValid 依赖的反射入口。
 */
class DemoTaskEnumTest {

    @Test
    void shouldResolveEnumByCode() {
        assertEquals(DemoTaskStatusEnum.DOING,
                DemoTaskStatusEnum.getByCode(DemoTaskStatusEnum.class, 1));
        assertEquals("已完成", DemoTaskStatusEnum.textOf(2));
        assertEquals(DemoTaskPriorityEnum.HIGH,
                DemoTaskPriorityEnum.getByCode(DemoTaskPriorityEnum.class, 3));
        assertNull(DemoTaskStatusEnum.textOf(99));
    }
}
