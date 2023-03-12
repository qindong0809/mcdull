package io.gitee.dqcer.mcdull.admin.model.vo.common;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

/**
 * 验证码视图对象
 *
 * @author dqcer
 * @since 2023/03/11
 */
public class CaptchaVO implements VO {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    private String uuid;

    /**
     * base64 img
     */
    private String img;

    /**
     * 启用验证码
     */
    private Boolean captchaEnabled;

    @Override
    public String toString() {
        return "CaptchaVO{" +
                "uuid='" + uuid + '\'' +
                ", img='" + img + '\'' +
                ", captchaEnabled=" + captchaEnabled +
                '}';
    }

    public Boolean getCaptchaEnabled() {
        return captchaEnabled;
    }

    public CaptchaVO setCaptchaEnabled(Boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public CaptchaVO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getImg() {
        return img;
    }

    public CaptchaVO setImg(String img) {
        this.img = img;
        return this;
    }
}
