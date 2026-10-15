package io.gitee.dqcer.mcdull.ai;

public record EmailClassification(Classification classification, String reasoning) {
    enum Classification {
        IMPORTANT, NOT_IMPORTANT
    }
}
