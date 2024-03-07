package com.management.leave.api;

import com.management.leave.model.Result;

/**
 * function about system login
 * @author zh
 */
public interface LoginService {
    /**
     * system login
     * @return
     */
    Result<String> login();

    /**
     * system login out
     * @return
     */
    Result<String> loginOut();

}
