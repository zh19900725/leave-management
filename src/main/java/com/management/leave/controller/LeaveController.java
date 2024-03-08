package com.management.leave.controller;

import com.management.leave.api.LeaveServiceApi;
import com.management.leave.common.enums.ActionEnum;
import com.management.leave.common.util.CommonUtils;
import com.management.leave.common.util.ThreadPoolUtil;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.model.dto.ApprovalDTO;
import com.management.leave.model.dto.LeaveFormListDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.dto.ResultDTO;
import com.management.leave.model.pojo.ApprovalRes;
import com.management.leave.model.pojo.EmployeeInfo;
import com.management.leave.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * leave controller
 * @author zh
 */
@RestController
@Slf4j
@RequestMapping("/sys/leave")
public class LeaveController implements LeaveServiceApi {
    @Autowired
    LeaveService leaveService;

    @Override
    @PostMapping("/submit")
    public ResultDTO<String> addOrUpdateForm(HttpServletRequest request, LeaveRequestDTO req) {
        // 参数校验
        Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getAction());
        Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getApplicantId());
        switch (req.getAction()){
            case EDIT:
            case CANCEL:
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getFormId());
                break;
            case SUBMIT:
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getStartTime());
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getEndTime());
                Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR,req.getReason());
        }
        EmployeeInfo loginInfo = CommonUtils.getLoginInfo(request);
        Assert.assertNotNull(ErrorInfo.ERROR_UNKNOWN_ERROR,loginInfo);
        boolean b = leaveService.addOrUpdateLeaveForm(loginInfo, req);
        // 如果保存草稿直接返回，如果是提交动作且操作成功通知审批人
        if (b && ActionEnum.SUBMIT.equals(req.getAction())) {
            ThreadPoolUtil.getInstance().execute(()->{
                log.info("send email to manager!");
                // todo 邮件发送，这里先用伪代码替代
            });
        }
        return null;
    }


    @Override
    @PostMapping("/approval")
    public ResultDTO<String> approvalLeaveForm(HttpServletRequest request,ApprovalDTO req) {
        // 参数校验
        Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR,req.getChangeStatus());
        Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR,req.getComments());
        EmployeeInfo loginInfo = CommonUtils.getLoginInfo(request);
        Assert.assertNotNull(ErrorInfo.ERROR_UNKNOWN_ERROR,loginInfo);
        ApprovalRes res = leaveService.approval(req, loginInfo);
        ThreadPoolUtil.getInstance().execute(()->{
            switch (res.getCurLevel()) {
                case 1:
                    // 即时通告：如果是1级审批通过，通知申请人
                    if (res.isNeedNextApproval()){
                        // todo 邮件和短信发送，通知通知2级审批人进行审批
                    } else {
                        // todo 邮件和短信发送，通知申请人申请通过
                    }
                    break;
                case 2:
                    // 即时通告：如果是2级审批通过，或者请求单被驳回，通知申请人
                    // todo 邮件和短信发送，通知申请人申请通过
                default:
                    log.error("approval result exception! res={}",res);
            }
        });

        return null;
    }

    @Override
    @PostMapping("/LeaveFormList")
    public ResultDTO<List<LeaveRequestDTO>> leaveFormList(LeaveFormListDTO req) {
        // 查询数据库，返回请假申请记录列表
        leaveService.getLeaveFormList(req);
        return null;
    }


}
