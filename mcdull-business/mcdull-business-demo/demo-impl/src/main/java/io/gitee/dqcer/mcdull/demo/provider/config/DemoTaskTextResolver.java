package io.gitee.dqcer.mcdull.demo.provider.config;

import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskPriorityEnum;
import io.gitee.dqcer.mcdull.demo.facade.model.enums.DemoTaskStatusEnum;

/**
 * 契约枚举文本解析器：把展示层翻译能力收敛到实现侧。
 */
public class DemoTaskTextResolver {

    /**
     * 状态编码转文本。
     *
     * @param code 状态码
     * @return 文本
     */
    public String statusText(Integer code) {
        return DemoTaskStatusEnum.textOf(code);
    }

    /**
     * 优先级编码转文本。
     *
     * @param code 优先级编码
     * @return 文本
     */
    public String priorityText(Integer code) {
        return DemoTaskPriorityEnum.textOf(code);
    }
}
