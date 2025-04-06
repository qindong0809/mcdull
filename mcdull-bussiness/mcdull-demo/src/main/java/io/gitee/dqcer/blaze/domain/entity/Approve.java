package io.gitee.dqcer.blaze.domain.entity;

public interface Approve {
    Integer getId();

    Integer getApprove();

    void setApprove(Integer Approve);

    String getApproveRemarks();

    void setApproveRemarks(String approveRemarks);

    Integer getResponsibleUserId();

    void setResponsibleUserId(Integer responsibleUserId);
}
