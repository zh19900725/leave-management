package com.management.leave.controller;

import com.management.leave.api.LeaveServiceApi;
import com.management.leave.common.enums.ActionEnum;
import com.management.leave.common.util.CommonUtils;
import com.management.leave.common.util.ThreadPoolUtil;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.exception.MyException;
import com.management.leave.model.dto.ApprovalReqDTO;
import com.management.leave.model.dto.LeaveFormListReqDTO;
import com.management.leave.model.dto.LeaveFormListResDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.dto.ResultDTO;
import com.management.leave.model.pojo.ApprovalRes;
import com.management.leave.model.pojo.EmployeeInfo;
import com.management.leave.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @PostMapping("/addOrUpdate")
    public ResultDTO<Boolean> addOrUpdateForm(HttpServletRequest request,@Validated LeaveRequestDTO req) {
        // 参数校验
        validatorParam(req);
        EmployeeInfo loginInfo = CommonUtils.getLoginInfo(request);
        Assert.assertNotNull(ErrorInfo.ERROR_UNKNOWN_ERROR,loginInfo);
        boolean b = leaveService.addOrUpdateLeaveForm(loginInfo, req);
        // 如果保存草稿直接返回，如果是提交动作且操作成功通知审批人
        if (b && ActionEnum.SUBMIT.getCode().equals(req.getAction())) {
            ThreadPoolUtil.getInstance().execute(()->{
                log.info("send email to manager!");
                // todo 邮件发送，这里先用伪代码替代
            });
        }
        if (b) {
            return ResultDTO.success(b);
        } else {
            return ResultDTO.failure(ErrorInfo.ERROR_UNKNOWN_ERROR);
        }
    }

    private static void validatorParam(LeaveRequestDTO req) {
        Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR, req.getAction());
        ActionEnum action = ActionEnum.query(req.getAction());
        Assert.assertNotNull(ErrorInfo.ERROR_ACTION_NOT_SUPPORT,action);
        switch (action){
            case EDIT:
            case CANCEL:
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR, req.getFormId());
                break;
            case SUBMIT:
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR, req.getStartTime());
                Assert.assertNotNull(ErrorInfo.ERROR_PARAM_ERROR, req.getEndTime());
                Assert.assertNotEmpty(ErrorInfo.ERROR_PARAM_ERROR, req.getReason());
            default:
                log.error("action exception {}",action);
                throw new MyException(ErrorInfo.ERROR_UNKNOWN_ERROR);
        }
    }


    @Override
    @PostMapping("/approval")
    public ResultDTO<Boolean> approval(HttpServletRequest request,@Validated ApprovalReqDTO req) {
        // 参数校验
        EmployeeInfo loginInfo = CommonUtils.getLoginInfo(request);
        Assert.assertNotNull(ErrorInfo.ERROR_UNKNOWN_ERROR,loginInfo);
        ApprovalRes res = leaveService.approval(req, loginInfo);
        ThreadPoolUtil.getInstance().execute(()->{
            switch (res.getCurLevel()) {
                case 1:
                    // 即时通告：如果是1级审批通过，通知申请人
                    if (res.isNeedNextApproval()){
                        // todo 邮件和短信发送，通知2级审批人进行审批
                        log.info("send email to senior manager");
                    } else {
                        // todo 邮件和短信发送，通知申请人申请通过
                        log.info("application process end ,send result email to applicant");
                    }
                    break;
                case 2:
                    // 即时通告：如果是2级审批通过，或者请求单被驳回，通知申请人
                    // todo 邮件和短信发送，通知申请人申请通过
                    log.info("application process end ,send result email to applicant");
                    break;
                default:
                    log.error("approval result exception! res={}",res);
            }
        });
        return ResultDTO.success(true);
    }

    @Override
    @PostMapping("/forms")
    public ResultDTO<LeaveFormListResDTO> leaveFormList(@Validated LeaveFormListReqDTO req) {
        return ResultDTO.success(leaveService.getLeaveFormList(req));
    }


}
