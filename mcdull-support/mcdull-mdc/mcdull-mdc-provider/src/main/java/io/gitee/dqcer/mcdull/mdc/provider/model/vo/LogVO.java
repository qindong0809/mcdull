package io.gitee.dqcer.mcdull.mdc.provider.model.vo;

import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogEntity;

import java.util.StringJoiner;

/**
 * 日志视图对象
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class LogVO extends LogEntity {

    private String accountIdStr;

    @Override
    public String toString() {
        return new StringJoiner(", ", LogVO.class.getSimpleName() + "[", "]")
                .add("accountIdStr='" + accountIdStr + "'")
                .add("id=" + id)
                .toString();
    }

    public String getAccountIdStr() {
        return accountIdStr;
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }
}
