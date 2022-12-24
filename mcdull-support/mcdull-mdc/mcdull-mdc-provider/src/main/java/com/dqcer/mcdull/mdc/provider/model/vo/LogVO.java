package com.dqcer.mcdull.mdc.provider.model.vo;

import com.dqcer.mcdull.mdc.provider.model.entity.LogDO;

import java.util.StringJoiner;

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
