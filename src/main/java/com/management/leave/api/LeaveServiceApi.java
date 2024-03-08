package com.management.leave.api;

import com.management.leave.model.dto.ApprovalDTO;
import com.management.leave.model.dto.LeaveFormListDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * function about leave request
 * @author zh
 */
public interface LeaveServiceApi {
    /**
     * submit leave request or update leave form
     * @param request we can get some login info from this param
     * @param req addOrUpdateForm request object
     * @return
     */
    ResultDTO<Boolean> addOrUpdateForm(HttpServletRequest request, LeaveRequestDTO req);


    /**
     * approval leave request
     * @param request we can get some login info from this param
     * @param req the approval request
     * @return
     */
    ResultDTO<String> approvalLeaveForm(HttpServletRequest request,ApprovalDTO req);

    /**
     * query leaveFormList
     * @param request we can get some login info from this param
     * @param req some query params when query leave form list
     * @return
     */
    ResultDTO<List<LeaveRequestDTO>> leaveFormList(LeaveFormListDTO req);
}
