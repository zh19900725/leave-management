package com.management.leave.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zh
 */
@Getter
@ToString
@AllArgsConstructor
public enum ActionEnum {
    /**
     * 编辑
     */
    EDIT("EDIT","编辑"),

    /**
     * 提交
     */
    SUBMIT("SUBMIT","提交"),

    /**
     * 撤销（撤销后还可以修改再次提交，删除后就查不到了）
     */
    CANCEL("CANCEL","撤销"),

    ;
    private final String code;
    private final String desc;


    public static ActionEnum query(String code) {
        for (ActionEnum value : ActionEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
