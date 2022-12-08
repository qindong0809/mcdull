package com.dqcer.mcdull.mdc.api.vo;

import com.dqcer.mcdull.mdc.api.entity.LogDO;

public class LogVO extends LogDO {

    private String accountIdStr;

    public String getAccountIdStr() {
        return accountIdStr;
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }
}
