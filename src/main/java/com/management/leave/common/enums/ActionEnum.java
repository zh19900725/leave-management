package com.management.leave.common.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ActionEnum {
    EDIT("EDIT","编辑"),
    SUBMIT("SUBMIT","提交"),
    CANCEL("CANCEL","撤销（撤销和删除的区别是，撤销后还可以修改再次提交，删除后就查不到了）"),
    DELETE("DELETE","删除"),
    ;
    String code;
    String desc;

    ActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActionEnum query(String code) {
        for (ActionEnum value : ActionEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}