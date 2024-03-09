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
    REJECTED("REJECTED","审批人驳回",false),
    AGREEMENT("AGREEMENT","审批人通过审批",false),

    CANCEL("CANCEL","用户撤销",false),
    CLOSE("CLOSE","删除",true),


    SUCCESS("SUCCESS","流程结束成功",true),
    WAITE_FIRST_CONFIRM("WAITE_FIRST_CONFIRM", "等待一次审批人审批",false),
    WAITE_SECOND_CONFIRM("WAITE_SECOND_CONFIRM", "等待二次审批人审批",false),
    ;
    /**
     * 状态码
     */
    String code;
    /**
     * 状态描述
     */
    String desc;
    /**
     * 是否终止状态（终止状态的单子不允许在操作）
     */
    Boolean isOver;

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
