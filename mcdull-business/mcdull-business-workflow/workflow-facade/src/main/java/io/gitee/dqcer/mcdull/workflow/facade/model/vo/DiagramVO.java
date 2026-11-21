package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程图数据。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String definitionKey;

    @Builder.Default
    private List<DiagramNodeVO> nodes = new ArrayList<>();

    @Builder.Default
    private List<DiagramEdgeVO> edges = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagramNodeVO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String nodeCode;

        private String nodeName;

        private Integer nodeType;

        private String coordinate;

        private String handlerPath;

        private String permissionFlag;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagramEdgeVO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String fromNodeCode;

        private String toNodeCode;

        private String label;

        private String condition;

        private String skipType;
    }
}
