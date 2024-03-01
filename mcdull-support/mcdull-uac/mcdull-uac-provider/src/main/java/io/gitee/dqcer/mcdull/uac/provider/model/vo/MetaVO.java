package io.gitee.dqcer.mcdull.uac.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.StringJoiner;

/**
 * 路由显示信息
 *
 * @author dqcer
 * @since 2024/03/01
 */
public class MetaVO implements VO {

    private static final long serialVersionUID = 1L;

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
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;


    @Override
    public String toString() {
        return new StringJoiner(", ", MetaVO.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("icon='" + icon + "'")
                .add("noCache=" + noCache)
                .add("link='" + link + "'")
                .toString();
    }

    public MetaVO() {
    }

    public MetaVO(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public MetaVO(String title, String icon, boolean noCache) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public MetaVO(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVO(String title, String icon, boolean noCache, String link) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
//        if (StrUtil.ishttp(link)) {
            this.link = link;
//        }
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
