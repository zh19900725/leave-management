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
public enum StatusEnum {
    /**
     * 表示审批人驳回
     */
    REJECTED("REJECTED",true),

    /**
     * 审批人通过当前审批（这个状态只表示当前审批人同意，不落库，由后台判断直接SUCCESS还是等待2级审批）
     */
    AGREEMENT("AGREEMENT",false),

    /**
     * 用户撤销申请
     */
    CANCEL("CANCEL",true),

    /**
     * 申请成功，流程结束
     */
    SUCCESS("SUCCESS",true),

    /**
     * 请求一级审批人审批
     */
    FIRST_CONFIRM("FIRST_CONFIRM",false),

    /**
     * 请求二级审批人审批
     */
    SECOND_CONFIRM("SECOND_CONFIRM",false),
    ;


    /**
     * 状态码
     */
    private final String code;
    /**
     * 是否终止状态（终止状态的单子不允许在操作）
     */
    private final Boolean isOver;

    /**
     * 根据code查询枚举值
     * @param code
     * @return
     */
    public static StatusEnum query(String code) {
        for (StatusEnum value : StatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
