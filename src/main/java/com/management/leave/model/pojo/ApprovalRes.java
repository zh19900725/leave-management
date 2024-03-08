package com.management.leave.model.pojo;

import lombok.Data;

/**
 * @author zh
 */
@Data
public class ApprovalRes {
    /**
     * 审批单id
     */
    private String formId;
    /**
     * 当前审批动作的提交结果，这里判断数据update是否成功，如果提交失败则不触发邮件推送
     */
    private int processRes;
    /**
     * 当前是第几级审批
     */
    private int curLevel;
    /**
     * 是否需要再进行下一级审批
     */
    private boolean isNeedNextApproval;

}
