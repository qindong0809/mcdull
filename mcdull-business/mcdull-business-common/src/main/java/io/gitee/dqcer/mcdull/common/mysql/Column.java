package io.gitee.dqcer.mcdull.common.mysql;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class Column {
    private String name;

    private String type;

    private String length;

    private String decimalDigits;

    private Boolean notNull;

    private Boolean primary;

    private String comment;

}
