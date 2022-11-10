package com.dqcer.cloud.model;


import java.util.Date;

/**
 * 服务日志
 *
 * @author dqcer
 * @date 2022/11/10
 */
public class ServiceLog {

    /**
     * id
     */
    private Long id;

    /**
     * 跟踪id
     */
    private String traceId;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求参数
     */
    private String reqArgs;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 时间
     */
    private long costTime;

    /**
     * 状态 1/成功 2/失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 结束时间
     */
    private Date endTime;

    @Override
    public String toString() {
        return "ServiceLog{" +
                "id=" + id +
                ", traceId='" + traceId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", reqArgs='" + reqArgs + '\'' +
                ", result='" + result + '\'' +
                ", costTime=" + costTime +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", endTime=" + endTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReqArgs() {
        return reqArgs;
    }

    public void setReqArgs(String reqArgs) {
        this.reqArgs = reqArgs;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
