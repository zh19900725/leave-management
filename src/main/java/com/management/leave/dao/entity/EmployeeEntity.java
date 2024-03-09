package com.management.leave.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Auto Generator
 * @since 2024-03-09
 */
@Getter
@Setter
@TableName("t_employee")
public class EmployeeEntity {

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
     * 上司
     */
    private Integer superiorId;

    /**
     * 系统登录密码
     */
    private String mobile;

    /**
     * 系统登录名
     */
    private String loginName;

    /**
     * 员工邮箱
     */
    private String email;


}
