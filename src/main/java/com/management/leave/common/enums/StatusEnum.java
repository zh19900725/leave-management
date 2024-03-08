package com.management.leave.common.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author zh
 */
@Getter
@ToString
public enum StatusEnum {
    DRAFT("Draft", "用户已保存未提交给审批人"),
    WAITE_FIRST_CONFIRM("FirstConfirm", "等待一次审批人审批"),
    WAITE_SECOND_CONFIRM("SecondConfirm", "等待二次审批人审批"),
    REJECTED("TurnDown","审批人驳回"),
    AGREEMENT("Agreement","审批人通过审批"),
    CANCEL("Cancel","用户撤销"),
    CLOSE("CLOSE","删除"),
    ;
    String code;
    String desc;

    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
