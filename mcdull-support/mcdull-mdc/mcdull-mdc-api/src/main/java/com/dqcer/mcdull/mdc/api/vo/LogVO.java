package com.dqcer.mcdull.mdc.api.vo;

import com.dqcer.mcdull.mdc.api.entity.LogEntity;

public class LogVO extends LogEntity {

    private String accountIdStr;

    public String getAccountIdStr() {
        return accountIdStr;
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }
}
