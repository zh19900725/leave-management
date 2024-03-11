package com.management.leave.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.leave.common.constats.Constants;
import com.management.leave.common.enums.ActionEnum;
import com.management.leave.common.enums.StatusEnum;
import com.management.leave.common.util.CommonUtils;
import com.management.leave.dao.entity.EmployeeEntity;
import com.management.leave.dao.entity.LeaveFormEntity;
import com.management.leave.dao.service.impl.EmployeeServiceDao;
import com.management.leave.dao.service.impl.LeaveFormServiceDao;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.exception.MyException;
import com.management.leave.model.dto.ApprovalReqDTO;
import com.management.leave.model.dto.LeaveFormListReqDTO;
import com.management.leave.model.dto.LeaveFormListResDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.dto.LeaveResDTO;
import com.management.leave.model.pojo.ApprovalRes;
import com.management.leave.model.pojo.EmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zh
 */
@Service
@Slf4j
public class LeaveService {

    @Autowired
    EmployeeServiceDao employeeServiceDao;

    @Autowired
    LeaveFormServiceDao leaveFormServiceDao;


    /**
     * 处理请假单，处理动作：提交、编辑、撤销
     *
     * @param loginInfo
     * @param leaveRequestDTO
     * @return
     * @throws MyException
     */
    @Transactional(rollbackFor = MyException.class)
    public boolean addOrUpdateLeaveForm(EmployeeInfo loginInfo, LeaveRequestDTO leaveRequestDTO) throws MyException {
        log.info("addOrUpdateLeaveForm params: {}", leaveRequestDTO);
        LeaveFormEntity leaveForm = null;
        int flag = 0;
        ActionEnum action = ActionEnum.query(leaveRequestDTO.getAction());
        Assert.assertNotNull(ErrorInfo.ERROR_ACTION_NOT_SUPPORT, action);
        switch (action) {
            case EDIT:
                flag = edit(leaveRequestDTO, loginInfo);
                break;
            case CANCEL:
                flag = cancel(leaveRequestDTO.getFormId(), loginInfo.getUserId());
                break;
            case SUBMIT:
                // 查询申请人信息
                flag = insert(loginInfo, leaveRequestDTO);
                break;
            default:
                Assert.assertTrue(ErrorInfo.ERROR_ACTION_NOT_SUPPORT, false);
        }
        log.info("addOrUpdateLeaveForm result: {}", flag > 0);
        return flag > 0;
    }

    /**
     * 编辑请假单
     *
     * @param leaveRequestDTO
     * @return
     */
    public int edit(LeaveRequestDTO leaveRequestDTO, EmployeeInfo loginUser) {
        log.info("edit leave form, params: {}", leaveRequestDTO);
        int flag;
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(leaveRequestDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus() == 0);
        // 只能修改save状态的申请
        Assert.assertTrue(ErrorInfo.ERROR_NO_PERMISSION, StatusEnum.SAVE.getCode().equals(leaveForm.getStatus()));
        StatusEnum statusNow = StatusEnum.query(leaveForm.getStatus());
        // 已经终止的订单不允许操作
        Assert.assertTrue(ErrorInfo.ERROR_FORM_STATUS_IS_TERMINATION, !statusNow.getIsOver());
        // 只有申请人可以编辑请假单，其他人的编辑请求驳回
        Assert.assertTrue(ErrorInfo.ERROR_NO_PERMISSION, leaveForm.getApplicantId() == loginUser.getUserId());

        leaveForm.setUpdateTime(new Date());
        leaveForm.setReason(leaveRequestDTO.getReason());
        leaveForm.setStartTime(new Date(leaveRequestDTO.getStartTime()));
        leaveForm.setEndTime(new Date(leaveRequestDTO.getEndTime()));
        leaveForm.setCurOperator(loginUser.getUserId());
        flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        log.info("edit leave form, result: {}", flag);
        return flag;
    }

    /**
     * 撤回请假单
     *
     * @param formId 请假单id
     * @return
     */
    public int cancel(Integer formId, String operatorId) {
        log.info("change leave form status, params: formId={}", formId);
        int flag;
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(formId);
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus() == 0);
        StatusEnum statusNow = StatusEnum.query(leaveForm.getStatus());
        // 已经终止的订单不允许操作
        Assert.assertTrue(ErrorInfo.ERROR_FORM_STATUS_IS_TERMINATION, !statusNow.getIsOver());

        leaveForm.setStatus(StatusEnum.CANCEL.getCode());
        leaveForm.setCurOperator(operatorId);
        leaveForm.setUpdateTime(new Date());
        leaveForm.setRowStatus(1);
        flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        log.info("change leave form status, result:{},", flag);
        return flag;
    }

    /**
     * 请假单审批
     *
     * @param approvalReqDTO
     * @param loginInfo
     * @return 表示当前是第几级审批，1表示1级审批，2表示2级审批
     */
    @Transactional(rollbackFor = MyException.class)
    public ApprovalRes approval(ApprovalReqDTO approvalReqDTO, EmployeeInfo loginInfo) {
        log.info("approval leave form, params: {},", approvalReqDTO);
        ApprovalRes approvalRes = new ApprovalRes();
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(approvalReqDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus() == 0);
        StatusEnum statusNow = StatusEnum.query(leaveForm.getStatus());
        // 已经终止的订单不允许操作
        Assert.assertTrue(ErrorInfo.ERROR_FORM_STATUS_IS_TERMINATION, !statusNow.getIsOver());

        if (StatusEnum.FIRST_CONFIRM.getCode().equals(leaveForm.getStatus())) {
            // 申请单旧有状态是1级等待审批状态,判断是否需要2级审批
            boolean isNeedNext = firstApproval(approvalReqDTO, loginInfo, leaveForm);
            approvalRes.setCurLevel(1);
            approvalRes.setNeedNextApproval(isNeedNext);
            if (isNeedNext) {
                EmployeeEntity employee = employeeServiceDao.getBaseMapper().selectById(leaveForm.getFirstApprover());
                if (employee != null) {
                    leaveForm.setCurOperator(String.valueOf(employee.getSuperiorId()));
                }
            }
        } else if (StatusEnum.SECOND_CONFIRM.getCode().equals(leaveForm.getStatus())) {
            // 申请单旧有状态是2级等待审批状态，则无论驳回还是同意流程都结束了
            secondApproval(approvalReqDTO, loginInfo, leaveForm);
            approvalRes.setCurLevel(2);
        }
        int flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        approvalRes.setProcessRes(flag);
        approvalRes.setFormId(approvalRes.getFormId());
        log.info("approval leave form, updateById.result: {},", flag > 0);
        return approvalRes;
    }

    private static void secondApproval(ApprovalReqDTO approvalReqDTO, EmployeeInfo loginInfo, LeaveFormEntity leaveForm) {
        // 审批人身份校验
        Assert.assertTrue(ErrorInfo.ERROR_NO_PERMISSION, loginInfo.getUserId().equals(String.valueOf(leaveForm.getCurOperator())));
        // 如果2级审批人，更新2级审批人批语
        //这一步校验是为了禁止跨级别审批，必须一级领导审批后才能流转给二级领导
        Assert.assertNotEmpty(ErrorInfo.ERROR_CROSS_LEVEL_APPROVAL, leaveForm.getFirstApprover());
        Assert.assertNotEmpty(ErrorInfo.ERROR_CROSS_LEVEL_APPROVAL, leaveForm.getFirstComment());
        // 更新2级审批人批语
        leaveForm.setSecondComment(approvalReqDTO.getComments());
        if (StatusEnum.AGREEMENT.getCode().equals(approvalReqDTO.getNewStatus())) {
            leaveForm.setStatus(StatusEnum.SUCCESS.getCode());
        } else {
            leaveForm.setStatus(approvalReqDTO.getNewStatus());
        }
        leaveForm.setCurOperator(loginInfo.getUserId());
        leaveForm.setUpdateTime(new Date());
    }

    private static boolean firstApproval(ApprovalReqDTO approvalReqDTO, EmployeeInfo loginInfo, LeaveFormEntity leaveForm) {
        // 审批人身份校验
        Assert.assertTrue(ErrorInfo.ERROR_NO_PERMISSION, loginInfo.getUserId().equals(String.valueOf(leaveForm.getFirstApprover())));
        // 如果1级审批人，更新1级审批人批语
        leaveForm.setFirstComment(approvalReqDTO.getComments());

        // 如果大于10天，且1级审批同意申请，则状态更改为待2级审批
        LocalDate localDateStart = CommonUtils.dateToLocalDate(leaveForm.getStartTime());
        LocalDate localDateEnd = CommonUtils.dateToLocalDate(leaveForm.getEndTime());
        long between = ChronoUnit.DAYS.between(localDateStart, localDateEnd);
        if (between >= Constants.TIME_BETWEEN && StatusEnum.AGREEMENT.getCode().equals(approvalReqDTO.getNewStatus())) {
            leaveForm.setStatus(StatusEnum.SECOND_CONFIRM.getCode());
        } else {
            if (StatusEnum.AGREEMENT.getCode().equals(approvalReqDTO.getNewStatus())) {
                leaveForm.setStatus(StatusEnum.SUCCESS.getCode());
            } else {
                leaveForm.setStatus(approvalReqDTO.getNewStatus());
            }
            leaveForm.setCurOperator(loginInfo.getUserId());
        }

        leaveForm.setUpdateTime(new Date());
        return between >= Constants.TIME_BETWEEN;
    }


    /**
     * 创建请假单
     *
     * @param loginInfo
     * @param leaveRequestDTO
     * @return
     */
    public int insert(EmployeeInfo loginInfo, LeaveRequestDTO leaveRequestDTO) {
        int flag;
        EmployeeEntity employeeEntity = employeeServiceDao.getBaseMapper().selectById(loginInfo.getUserId());
        Assert.assertNotNull(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity);
        Assert.assertTrue(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity.getRowStatus() == 0);
        checkExist(leaveRequestDTO, employeeEntity.getId());

        // 请假单不存在，创建请假单
        LeaveFormEntity leaveFormEntity = new LeaveFormEntity();
        leaveFormEntity.setApplicantId(loginInfo.getUserId());
        leaveFormEntity.setCreateTime(new Date());
        leaveFormEntity.setUpdateTime(new Date());
        leaveFormEntity.setStatus(StatusEnum.FIRST_CONFIRM.getCode());
        leaveFormEntity.setStartTime(new Date(leaveRequestDTO.getStartTime()));
        leaveFormEntity.setEndTime(new Date(leaveRequestDTO.getEndTime()));
        leaveFormEntity.setReason(leaveRequestDTO.getReason());
        leaveFormEntity.setFirstApprover(employeeEntity.getSuperiorId());
        leaveFormEntity.setCurOperator(String.valueOf(employeeEntity.getSuperiorId()));

        LocalDate localDateStart = CommonUtils.dateToLocalDate(new Date(leaveRequestDTO.getStartTime()));
        LocalDate localDateEnd = CommonUtils.dateToLocalDate(new Date(leaveRequestDTO.getEndTime()));
        long between = ChronoUnit.DAYS.between(localDateStart, localDateEnd);
        if (between >= Constants.TIME_BETWEEN) {
            EmployeeEntity employee = employeeServiceDao.getBaseMapper().selectById(employeeEntity.getSuperiorId());
            leaveFormEntity.setSecondApprover(employee.getSuperiorId());
        }
        flag = leaveFormServiceDao.getBaseMapper().insert(leaveFormEntity);
        return flag;
    }

    public List<LeaveFormEntity> checkExist(LeaveRequestDTO leaveRequestDTO, Integer employeeId) {
        QueryWrapper<LeaveFormEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("applicant_Id", employeeId);
        wrapper.le("start_time", new Date(leaveRequestDTO.getStartTime()));
        wrapper.ge("end_time", new Date(leaveRequestDTO.getEndTime()));
        wrapper.eq("row_status", 0);
        List<LeaveFormEntity> list = leaveFormServiceDao.getBaseMapper().selectList(wrapper);
        // 如果已经存在请假单包含了这个段请假时间，返回报错
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_IS_STILL_EXIST, CollectionUtils.isEmpty(list));
        return list;
    }


    /**
     * 查询请假单列表
     *
     * @param req
     * @return
     */
    public LeaveFormListResDTO getLeaveFormList(LeaveFormListReqDTO req) {
        log.info("getLeaveFormList req {}", req);
        LeaveFormListResDTO resPage = new LeaveFormListResDTO();
        List<LeaveResDTO> forms = new ArrayList<>();
        resPage.setForms(forms);
        QueryWrapper<LeaveFormEntity> wrapper = new QueryWrapper<>();
        if (null != req.getApplicantId()) {
            wrapper.eq("applicant_Id", req.getApplicantId());
        }
        if (null != req.getApproverId()) {
            wrapper.eq("cur_operator", req.getApproverId());
        }
        if (null != req.getStartTime() && req.getStartTime() > 0) {
            wrapper.ge("start_time", new Date(req.getStartTime()));
        }
        if (null != req.getEndTime() && req.getEndTime() > 0) {
            wrapper.le("end_time", new Date(req.getEndTime()));
        }
        if (null != req.getStatus()) {
            wrapper.le("status", req.getStatus());
        }
        wrapper.eq("row_status", "0");
        Page<LeaveFormEntity> pageParam = new Page<>(req.getPageNo(), req.getPageSize());
        Page<LeaveFormEntity> pageData = leaveFormServiceDao.getBaseMapper().selectPage(pageParam, wrapper);
        resPage.setTotalRows(pageData.getTotal());
        resPage.setTotalPage(pageData.getPages());
        for (LeaveFormEntity leaveFormEntity : pageData.getRecords()) {
            LeaveResDTO leaveDTO = leaveEntityToDTO(leaveFormEntity);
            if (leaveDTO != null) {
                forms.add(leaveDTO);
            }
        }
        log.debug("getLeaveFormList res {}", resPage);
        return resPage;
    }

    private LeaveResDTO leaveEntityToDTO(LeaveFormEntity dto) {
        if (dto == null) {
            return null;
        }
        LeaveResDTO leaveDTO = new LeaveResDTO();
        leaveDTO.setId(dto.getId());
        leaveDTO.setApplicantId(dto.getApplicantId());
        leaveDTO.setCreateTime(dto.getCreateTime());
        leaveDTO.setUpdateTime(dto.getUpdateTime());
        leaveDTO.setStatus(dto.getStatus());
        leaveDTO.setStartTime(dto.getStartTime());
        leaveDTO.setEndTime(dto.getEndTime());
        leaveDTO.setReason(dto.getReason());
        leaveDTO.setFirstApprover(dto.getFirstApprover());
        leaveDTO.setSecondApprover(dto.getSecondApprover());
        leaveDTO.setFirstComment(dto.getFirstComment());
        leaveDTO.setSecondComment(dto.getSecondComment());
        leaveDTO.setCurOperator(dto.getCurOperator());
        leaveDTO.setRowStatus(dto.getRowStatus());
        return leaveDTO;
    }

    /**
     * get submit but unfinished forms
     *
     * @param start
     * @param end
     * @return
     */
    public List<LeaveFormEntity> getUnfinishedForm(Date start, Date end) {
        log.info("getUnfinishedForm req, start={},end={}", start, end);
        if (start == null || end == null) {
            return null;
        }
        QueryWrapper<LeaveFormEntity> wrapper = new QueryWrapper<>();
        wrapper.ge("start_time", start);
        wrapper.ge("end_time", end);
        wrapper.ge("row_status", 0);
        wrapper.eq("status", StatusEnum.FIRST_CONFIRM)
                .or()
                .eq("status", StatusEnum.SECOND_CONFIRM);
        List<LeaveFormEntity> leaveFormEntities = leaveFormServiceDao.getBaseMapper().selectList(wrapper);
        return leaveFormEntities;
    }


}
