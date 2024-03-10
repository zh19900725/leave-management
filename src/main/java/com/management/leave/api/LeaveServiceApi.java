package com.management.leave.api;

import com.management.leave.model.dto.ApprovalReqDTO;
import com.management.leave.model.dto.LeaveFormListReqDTO;
import com.management.leave.model.dto.LeaveFormListResDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.dto.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * function about leave request
 * @author zh
 */
@Api(value = "LeaveServiceApi", tags = {"请假单操作接口"})
public interface LeaveServiceApi {
    /**
     * submit leave request or update leave form
     * @param request we can get some login info from this param
     * @param req addOrUpdateForm request object
     * @return
     */
    @ApiOperation("提交或更新请假单")
    ResultDTO<Boolean> addOrUpdateForm(HttpServletRequest request, LeaveRequestDTO req);


    /**
     * approval leave request
     * @param request we can get some login info from this param
     * @param req the approval request
     * @return
     */
    @ApiOperation("审批")
    ResultDTO<Boolean> approval(HttpServletRequest request, ApprovalReqDTO req);

    /**
     * query leaveFormList
     * @param req some query params when query leave form list
     * @return
     */
    @ApiOperation("分页查询请假单")
    ResultDTO<LeaveFormListResDTO> leaveFormList(LeaveFormListReqDTO req);
}
