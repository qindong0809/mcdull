package io.gitee.dqcer.mcdull.blaze.domain.vo;

import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;

public class SelectVO extends LabelValueVO<Integer, String> {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public SelectVO(Integer value, String label) {
        super(value, label);
    }
}
