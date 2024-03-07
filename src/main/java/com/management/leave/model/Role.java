package com.management.leave.model;

import lombok.Data;

/**
 * @author zh
 */
@Data
public class Role {
    /**
     * role id
     */
    private String roleId;
    /**
     * role name
     */
    private String roleName;
    /**
     * role description
     */
    private String desc;
    /**
     * role permission
     */
    private Object resource;

}
