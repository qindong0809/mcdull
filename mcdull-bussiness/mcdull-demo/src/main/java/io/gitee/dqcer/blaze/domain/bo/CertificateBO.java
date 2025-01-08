package io.gitee.dqcer.blaze.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class CertificateBO {

    private Integer code;

    private String name;

    private List<Major> majorList;

    @Data
    public static class Major {

        private Integer code;

        private String name;
    }
}
