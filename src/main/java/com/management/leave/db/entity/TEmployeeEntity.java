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
@TableName("t_employee")
public class TEmployeeEntity {

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String employeeName;

    /**
     * 角色id,1普通职员，2主管，3高级主管
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态，0正常，1删除
     */
    private Integer rowStatus;

    /**
     * 上级主管
     */
    private Integer superiorId;


    /**
     * 系统登录名
     */
    private String loginName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String mobile;

}
