package com.management.leave.common.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ActionEnum {
    EDIT("EDIT","编辑：对应更新操作"),
    SUBMIT("SUBMIT","提交：对应新增操作"),
    CANCEL("CANCEL","撤销：对应逻辑删除操作"),
    ;
    String code;
    String desc;

    ActionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
