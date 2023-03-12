package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

/**
 * 路由显示信息
 *
 * @author dqcer
 * @since 2023/03/12
 */
public class MetaVO implements VO {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private Boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    @Override
    public String toString() {
        return "MetaVO{" +
                "title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", noCache=" + noCache +
                ", link='" + link + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public MetaVO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public MetaVO setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public boolean isNoCache() {
        return noCache == null ? false : noCache;
    }

    public MetaVO setNoCache(boolean noCache) {
        this.noCache = noCache;
        return this;
    }

    public String getLink() {
        return link;
    }

    public MetaVO setLink(String link) {
        this.link = link;
        return this;
    }
}
