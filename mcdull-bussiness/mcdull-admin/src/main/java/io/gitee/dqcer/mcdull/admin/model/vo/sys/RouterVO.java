package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author dqcer
 * @since 2023/03/12
 */
public class RouterVO implements VO {

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVO meta;

    /**
     * 子路由
     */
    private List<RouterVO> children;

    @Override
    public String toString() {
        return "RouterVO{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", hidden=" + hidden +
                ", redirect='" + redirect + '\'' +
                ", component='" + component + '\'' +
                ", query='" + query + '\'' +
                ", alwaysShow=" + alwaysShow +
                ", meta=" + meta +
                ", children=" + children +
                '}';
    }

    public String getName() {
        return name;
    }

    public RouterVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RouterVO setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public RouterVO setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public String getRedirect() {
        return redirect;
    }

    public RouterVO setRedirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getComponent() {
        return component;
    }

    public RouterVO setComponent(String component) {
        this.component = component;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public RouterVO setQuery(String query) {
        this.query = query;
        return this;
    }

    public Boolean getAlwaysShow() {
        return alwaysShow;
    }

    public RouterVO setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    public MetaVO getMeta() {
        return meta;
    }

    public RouterVO setMeta(MetaVO meta) {
        this.meta = meta;
        return this;
    }

    public List<RouterVO> getChildren() {
        return children;
    }

    public RouterVO setChildren(List<RouterVO> children) {
        this.children = children;
        return this;
    }
}
