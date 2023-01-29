package io.gitee.dqcer.cloud.model;

import java.io.Serializable;

/**
 * gc 日志详细信息
 *
 * @author dqcer
 * @since 2022/11/11
 */
@SuppressWarnings("unused")
public class ServiceGcLog implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 服务日志id
     */
    private Long serviceLogId;

    /**
     * 名字
     */
    private String name;

    /**
     * 内存池名称
     */
    private String memoryPoolNames;

    /**
     * gc 次数
     */
    private Long count;

    /**
     * gc 时间
     */
    private Long time;

    /**
     * 类型 1/之前 2/之后
     */
    private Integer type;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceLogId() {
        return serviceLogId;
    }

    public void setServiceLogId(Long serviceLogId) {
        this.serviceLogId = serviceLogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemoryPoolNames() {
        return memoryPoolNames;
    }

    public void setMemoryPoolNames(String memoryPoolNames) {
        this.memoryPoolNames = memoryPoolNames;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
