package io.gitee.dqcer.mdc.provider.model.vo;

import io.gitee.dqcer.mdc.provider.model.entity.LogDO;

import java.util.StringJoiner;

/**
 * 日志视图对象
 *
 * @author dqcer
 * @version 2022/12/26
 */
public class LogVO extends LogDO {

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
