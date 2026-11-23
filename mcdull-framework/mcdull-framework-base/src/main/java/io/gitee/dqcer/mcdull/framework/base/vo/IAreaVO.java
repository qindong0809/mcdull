package io.gitee.dqcer.mcdull.framework.base.vo;

/**
 * 区域编码/名称契约。
 * <p>
 * 稳定的基础结构（省/市编码与名称），由 common 的区域回填工具使用。
 * 上移至 framework-base，使 common 无需依赖任何业务 facade。
 *
 * @author dqcer
 */
public interface IAreaVO {

    String getProvincesCode();

    void setProvincesName(String provincesName);

    String getCityCode();

    void setCityName(String cityName);
}
