package io.gitee.dqcer.cloud.model;


import java.util.Date;

/**
 * 服务日志
 *
 * @author dqcer
 * @since  2022/11/10
 */
@SuppressWarnings("unused")
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


    /**堆内存 ：初始化大小、已使用、提交的内存量，保证Java虚拟机可以使用这个内存量
     * 、 最大值， */
    private Long beforeHeadMemoryInit;
    private Long beforeHeadMemoryUsed;
    private Long beforeHeadMemoryCommitted;
    private Long beforeHeadMemoryMax;
    /**
     *  beforeHeadMemoryCommitted * 100 / beforeHeadMemoryCommitted
     */
    private Long beforeHeadRate;

    /**非堆内存*/
    private Long beforeNonHeadMemoryInit;
    private Long beforeNonHeadMemoryUsed;
    private Long beforeNonHeadMemoryCommitted;
    private Long beforeNonHeadMemoryMax;
    private Long beforeNonHeadRate;

    /**所花费的内存信息*/
    private Long costHeadMemoryInit;
    private Long costHeadMemoryUsed;
    private Long costHeadMemoryCommitted;
    private Long costHeadMemoryMax;
    private Long costHeadRate;

    private Long costNonHeadMemoryInit;
    private Long costNonHeadMemoryUsed;
    private Long costNonHeadMemoryCommitted;
    private Long costNonHeadMemoryMax;
    private Long costNonHeadRate;


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
                ", beforeHeadMemoryInit=" + beforeHeadMemoryInit +
                ", beforeHeadMemoryUsed=" + beforeHeadMemoryUsed +
                ", beforeHeadMemoryCommitted=" + beforeHeadMemoryCommitted +
                ", beforeHeadMemoryMax=" + beforeHeadMemoryMax +
                ", beforeHeadRate=" + beforeHeadRate +
                ", beforeNonHeadMemoryInit=" + beforeNonHeadMemoryInit +
                ", beforeNonHeadMemoryUsed=" + beforeNonHeadMemoryUsed +
                ", beforeNonHeadMemoryCommitted=" + beforeNonHeadMemoryCommitted +
                ", beforeNonHeadMemoryMax=" + beforeNonHeadMemoryMax +
                ", beforeNonHeadRate=" + beforeNonHeadRate +
                ", costHeadMemoryInit=" + costHeadMemoryInit +
                ", costHeadMemoryUsed=" + costHeadMemoryUsed +
                ", costHeadMemoryCommitted=" + costHeadMemoryCommitted +
                ", costHeadMemoryMax=" + costHeadMemoryMax +
                ", costHeadRate=" + costHeadRate +
                ", costNonHeadMemoryInit=" + costNonHeadMemoryInit +
                ", costNonHeadMemoryUsed=" + costNonHeadMemoryUsed +
                ", costNonHeadMemoryCommitted=" + costNonHeadMemoryCommitted +
                ", costNonHeadMemoryMax=" + costNonHeadMemoryMax +
                ", costNonHeadRate=" + costNonHeadRate +
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

    public Long getBeforeHeadMemoryInit() {
        return beforeHeadMemoryInit;
    }

    public void setBeforeHeadMemoryInit(Long beforeHeadMemoryInit) {
        this.beforeHeadMemoryInit = beforeHeadMemoryInit;
    }

    public Long getBeforeHeadMemoryUsed() {
        return beforeHeadMemoryUsed;
    }

    public void setBeforeHeadMemoryUsed(Long beforeHeadMemoryUsed) {
        this.beforeHeadMemoryUsed = beforeHeadMemoryUsed;
    }

    public Long getBeforeHeadMemoryCommitted() {
        return beforeHeadMemoryCommitted;
    }

    public void setBeforeHeadMemoryCommitted(Long beforeHeadMemoryCommitted) {
        this.beforeHeadMemoryCommitted = beforeHeadMemoryCommitted;
    }

    public Long getBeforeHeadMemoryMax() {
        return beforeHeadMemoryMax;
    }

    public void setBeforeHeadMemoryMax(Long beforeHeadMemoryMax) {
        this.beforeHeadMemoryMax = beforeHeadMemoryMax;
    }

    public Long getBeforeHeadRate() {
        return beforeHeadRate;
    }

    public void setBeforeHeadRate(Long beforeHeadRate) {
        this.beforeHeadRate = beforeHeadRate;
    }

    public Long getBeforeNonHeadMemoryInit() {
        return beforeNonHeadMemoryInit;
    }

    public void setBeforeNonHeadMemoryInit(Long beforeNonHeadMemoryInit) {
        this.beforeNonHeadMemoryInit = beforeNonHeadMemoryInit;
    }

    public Long getBeforeNonHeadMemoryUsed() {
        return beforeNonHeadMemoryUsed;
    }

    public void setBeforeNonHeadMemoryUsed(Long beforeNonHeadMemoryUsed) {
        this.beforeNonHeadMemoryUsed = beforeNonHeadMemoryUsed;
    }

    public Long getBeforeNonHeadMemoryCommitted() {
        return beforeNonHeadMemoryCommitted;
    }

    public void setBeforeNonHeadMemoryCommitted(Long beforeNonHeadMemoryCommitted) {
        this.beforeNonHeadMemoryCommitted = beforeNonHeadMemoryCommitted;
    }

    public Long getBeforeNonHeadMemoryMax() {
        return beforeNonHeadMemoryMax;
    }

    public void setBeforeNonHeadMemoryMax(Long beforeNonHeadMemoryMax) {
        this.beforeNonHeadMemoryMax = beforeNonHeadMemoryMax;
    }

    public Long getBeforeNonHeadRate() {
        return beforeNonHeadRate;
    }

    public void setBeforeNonHeadRate(Long beforeNonHeadRate) {
        this.beforeNonHeadRate = beforeNonHeadRate;
    }

    public Long getCostHeadMemoryInit() {
        return costHeadMemoryInit;
    }

    public void setCostHeadMemoryInit(Long costHeadMemoryInit) {
        this.costHeadMemoryInit = costHeadMemoryInit;
    }

    public Long getCostHeadMemoryUsed() {
        return costHeadMemoryUsed;
    }

    public void setCostHeadMemoryUsed(Long costHeadMemoryUsed) {
        this.costHeadMemoryUsed = costHeadMemoryUsed;
    }

    public Long getCostHeadMemoryCommitted() {
        return costHeadMemoryCommitted;
    }

    public void setCostHeadMemoryCommitted(Long costHeadMemoryCommitted) {
        this.costHeadMemoryCommitted = costHeadMemoryCommitted;
    }

    public Long getCostHeadMemoryMax() {
        return costHeadMemoryMax;
    }

    public void setCostHeadMemoryMax(Long costHeadMemoryMax) {
        this.costHeadMemoryMax = costHeadMemoryMax;
    }

    public Long getCostHeadRate() {
        return costHeadRate;
    }

    public void setCostHeadRate(Long costHeadRate) {
        this.costHeadRate = costHeadRate;
    }

    public Long getCostNonHeadMemoryInit() {
        return costNonHeadMemoryInit;
    }

    public void setCostNonHeadMemoryInit(Long costNonHeadMemoryInit) {
        this.costNonHeadMemoryInit = costNonHeadMemoryInit;
    }

    public Long getCostNonHeadMemoryUsed() {
        return costNonHeadMemoryUsed;
    }

    public void setCostNonHeadMemoryUsed(Long costNonHeadMemoryUsed) {
        this.costNonHeadMemoryUsed = costNonHeadMemoryUsed;
    }

    public Long getCostNonHeadMemoryCommitted() {
        return costNonHeadMemoryCommitted;
    }

    public void setCostNonHeadMemoryCommitted(Long costNonHeadMemoryCommitted) {
        this.costNonHeadMemoryCommitted = costNonHeadMemoryCommitted;
    }

    public Long getCostNonHeadMemoryMax() {
        return costNonHeadMemoryMax;
    }

    public void setCostNonHeadMemoryMax(Long costNonHeadMemoryMax) {
        this.costNonHeadMemoryMax = costNonHeadMemoryMax;
    }

    public Long getCostNonHeadRate() {
        return costNonHeadRate;
    }

    public void setCostNonHeadRate(Long costNonHeadRate) {
        this.costNonHeadRate = costNonHeadRate;
    }
}
