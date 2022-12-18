package com.dqcer.mcdull.mdc.provider.model.vo;

import com.dqcer.mcdull.mdc.provider.model.entity.LogDO;

public class LogVO extends LogDO {

    private String accountIdStr;

    public String getAccountIdStr() {
        return accountIdStr;
    }

    public void setAccountIdStr(String accountIdStr) {
        this.accountIdStr = accountIdStr;
    }
}
