package com.management.leave.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Auto Generator
 * @since 2024-03-08
 */
@Getter
@Setter
@TableName("t_leave_form")
public class TLeaveFormEntity {

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 申请人，对应员工表的员工id
     */
    private String applicantId;

    /**
     * 请假单创建时间
     */
    private Date createTime;

    /**
     * 请假单更新时间
     */
    private Date updateTime;

    /**
     * 请假单状态，默认为待1级确认
     */
    private String status;

    /**
     * 请假开始时间
     */
    private Date startTime;

    /**
     * 请假结束时间
     */
    private Date endTime;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 一级审批人
     */
    private Integer firstApprover;

    /**
     * 二级审批人
     */
    private Integer secondApprover;

    /**
     * 一级审批意见
     */
    private String firstComment;

    /**
     * 二级审批意见
     */
    private String secondComment;

    /**
     * 最后更新申请单的操作人信息
     */
    private String curOperator;

    /**
     * 状态：0正常，1删除
     */
    private int rowStatus;


}
