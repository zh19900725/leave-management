package com.management.leave.api;

import com.alibaba.fastjson.JSONObject;
import com.management.leave.model.dto.LoginReq;
import com.management.leave.model.dto.ResultDTO;

/**
 * function about system login
 * @author zh
 */
public interface LoginServiceApi {
    /**
     * system login
     * @param req we can get some login info from this param
     * @return token
     */
    ResultDTO<String> login(LoginReq req);


    /**
     * get phone code
     * @param phone
     * @return code
     */
    ResultDTO<String> sendCode(JSONObject phone);

}
