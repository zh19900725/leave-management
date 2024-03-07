package com.management.leave.common;

/**
 * @author zh
 */
public enum StatusEnum {
    FIRST_CONFIRM("FirstConfirm", "Waiting for confirmation from the first level approver"),
    SECOND_CONFIRM("SecondConfirm", "Waiting for confirmation from the second level approver"),
    REJECTED("TurnDown","Your vacation request has been rejected"),
    AGREEMENT("AGREEMENT","Your leave application has been approved"),
    ;
    String code;
    String desc;

    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
