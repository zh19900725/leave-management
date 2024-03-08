package com.management.leave.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.leave.common.enums.StatusEnum;
import com.management.leave.common.util.CommonUtils;
import com.management.leave.db.entity.TEmployeeEntity;
import com.management.leave.db.entity.TLeaveFormEntity;
import com.management.leave.db.service.impl.TEmployeeServiceDao;
import com.management.leave.db.service.impl.TLeaveFormServiceDao;
import com.management.leave.exception.Assert;
import com.management.leave.exception.ErrorInfo;
import com.management.leave.exception.MyException;
import com.management.leave.model.dto.ApprovalDTO;
import com.management.leave.model.dto.LeaveFormListDTO;
import com.management.leave.model.dto.LeaveRequestDTO;
import com.management.leave.model.pojo.ApprovalRes;
import com.management.leave.model.pojo.EmployeeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @author zh
 */
@Service
@Slf4j
public class LeaveService {

    @Autowired
    TEmployeeServiceDao employeeServiceIDao;

    @Autowired
    TLeaveFormServiceDao leaveFormServiceDao;


    /**
     * 处理请假单，处理动作：提交、编辑、撤销
     * @param loginInfo
     * @param leaveRequestDTO
     * @return
     * @throws MyException
     */
    @Transactional(rollbackFor= MyException.class)
    public boolean addOrUpdateLeaveForm(EmployeeInfo loginInfo, LeaveRequestDTO leaveRequestDTO) throws MyException {
        log.info("addOrUpdateLeaveForm params: {}",leaveRequestDTO);
        TLeaveFormEntity leaveForm = null;
        int flag = 0;
        switch (leaveRequestDTO.getAction()) {
            case EDIT:
                flag = edit(leaveRequestDTO,loginInfo);
            case CANCEL:
                flag = cancel(leaveRequestDTO.getFormId(),loginInfo);
                break;
            case SUBMIT:
                // 查询申请人信息
                flag = insert(loginInfo, leaveRequestDTO);
                break;
            default:
                Assert.assertTrue(ErrorInfo.ERROR_ACTION_NOT_SUPPORT, false);
        }
        log.info("addOrUpdateLeaveForm result: {}",flag > 0);
        return flag > 0;
    }

    /**
     * 编辑请假单
     * @param leaveRequestDTO
     * @return
     */
    public int edit(LeaveRequestDTO leaveRequestDTO, EmployeeInfo loginInfo) {
        log.info("edit leave form, params: {}",leaveRequestDTO);
        int flag;
        TLeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(leaveRequestDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        leaveForm.setUpdateTime(new Date());
        leaveForm.setReason(leaveRequestDTO.getReason());
        leaveForm.setStartTime(leaveRequestDTO.getStartTime());
        leaveForm.setEndTime(leaveRequestDTO.getEndTime());
        leaveForm.setCurOperator(loginInfo.getUserName());
        flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        log.info("edit leave form, result: {}",flag);
        return flag;
    }

    /**
     * 更改请假单状态，撤销、驳回或审批通过
     * @param formId 请假单id
     * @return
     */
    public int cancel(Integer formId, EmployeeInfo loginInfo) {
        log.info("change leave form status, params: formId={}",formId);
        int flag;
        TLeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(formId);
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        leaveForm.setStatus(StatusEnum.CANCEL.getCode());
        leaveForm.setCurOperator(loginInfo.getUserName());
        leaveForm.setUpdateTime(new Date());
        flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        log.info("change leave form status, result:{},",flag);
        return flag;
    }

    /**
     * 请假单审批
     * @param approvalDTO
     * @param loginInfo
     * @return 表示当前是第几级审批，1表示1级审批，2表示2级审批
     */
    @Transactional(rollbackFor = MyException.class)
    public ApprovalRes approval(ApprovalDTO approvalDTO, EmployeeInfo loginInfo){
        log.info("approval leave form, params: {},",approvalDTO);
        ApprovalRes approvalRes = new ApprovalRes();
        TLeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(approvalDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        leaveForm.setStatus(approvalDTO.getChangeStatus().getCode());
        if (StatusEnum.WAITE_FIRST_CONFIRM.getCode().equals(leaveForm.getStatus())){
            // 如果1级审批人，更新1级审批人批语
            leaveForm.setFirstComment(approvalDTO.getComments());
            approvalRes.setCurLevel(1);
        } else if (StatusEnum.WAITE_SECOND_CONFIRM.getCode().equals(leaveForm.getStatus())) {
            // 如果2级审批人，更新2级审批人批语
            //这一步校验是为了禁止跨级别审批，必须一级领导审批后才能流转给二级领导
            Assert.assertNotEmpty(ErrorInfo.ERROR_CROSS_LEVEL_APPROVAL,leaveForm.getFirstApprover());
            Assert.assertNotEmpty(ErrorInfo.ERROR_CROSS_LEVEL_APPROVAL,leaveForm.getFirstComment());
            // 更新2级审批人批语
            leaveForm.setSecondComment(approvalDTO.getComments());
            approvalRes.setCurLevel(2);
        }
        int flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        approvalRes.setProcessRes(flag);
        approvalRes.setFormId(approvalRes.getFormId());
        // 时差大于10天，则需要进行一下级审批
        if (approvalRes.getCurLevel()==1) {
            LocalDate localDateStart = CommonUtils.dateToLocalDate(leaveForm.getStartTime());
            LocalDate localDateEnd = CommonUtils.dateToLocalDate(leaveForm.getEndTime());
            long between = ChronoUnit.DAYS.between(localDateStart, localDateEnd);
            approvalRes.setNeedNextApproval(between>=10);
            leaveForm.setCurOperator(loginInfo.getUserName());
            leaveForm.setUpdateTime(new Date());
        }
        log.info("approval leave form, updateById.result: {},",flag>0);
        return approvalRes;
    }


    /**
     * 创建请假单
     * @param loginInfo
     * @param leaveRequestDTO
     * @return
     */
    public int insert(EmployeeInfo loginInfo, LeaveRequestDTO leaveRequestDTO) {
        int flag;
        TEmployeeEntity tEmployeeEntity = employeeServiceIDao.getBaseMapper().selectById(leaveRequestDTO.getApplicantId());
        Assert.assertNotNull(ErrorInfo.ERROR_APPLICANT_NOT_EXIST, tEmployeeEntity);

        QueryWrapper<TLeaveFormEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("employee_name", tEmployeeEntity.getEmployeeName());
        wrapper.eq("start_time", leaveRequestDTO.getStartTime());
        wrapper.eq("end_time", leaveRequestDTO.getEndTime());
        TLeaveFormEntity leaveFormEntity = leaveFormServiceDao.getBaseMapper().selectOne(wrapper);
        // 如果请假单已经存在了返回报错
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_IS_STILL_EXIST, leaveFormEntity == null);

        // 请假单不存在，创建请假单
        leaveFormEntity = new TLeaveFormEntity();
        leaveFormEntity.setApplicantId(leaveRequestDTO.getApplicantId());
        leaveFormEntity.setCreateTime(new Date());
        leaveFormEntity.setUpdateTime(new Date());
        leaveFormEntity.setStatus(StatusEnum.WAITE_FIRST_CONFIRM.getCode());
        leaveFormEntity.setStartTime(leaveRequestDTO.getStartTime());
        leaveFormEntity.setEndTime(leaveRequestDTO.getEndTime());
        leaveFormEntity.setReason(leaveRequestDTO.getReason());
        leaveFormEntity.setFirstApprover(tEmployeeEntity.getSuperiorId());
        leaveFormEntity.setCurOperator(loginInfo.getUserName());
        flag = leaveFormServiceDao.getBaseMapper().insert(leaveFormEntity);
        return flag;
    }
    
    public List<TLeaveFormEntity> getLeaveFormList(LeaveFormListDTO req){
        log.info("getLeaveFormList req {}",req);
        TEmployeeEntity tEmployeeEntity = employeeServiceIDao.getBaseMapper().selectById(req.getUserId());
        Assert.assertNotNull(ErrorInfo.ERROR_APPLICANT_NOT_EXIST, tEmployeeEntity);
        
        QueryWrapper<TLeaveFormEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(tEmployeeEntity.getEmployeeName())){
            wrapper.eq("employee_name", tEmployeeEntity.getEmployeeName());
        }
        if ( null!=req.getStartTime()){
            wrapper.ge("start_time", req.getStartTime());
        }
        if ( null!=req.getEndTime()){
            wrapper.le("end_time", req.getEndTime());
        }
        if (null!=req.getStatus()){
            wrapper.le("status", req.getStatus());

        }
        List<TLeaveFormEntity> tLeaveFormEntities = leaveFormServiceDao.getBaseMapper().selectList(wrapper);
        log.debug("getLeaveFormList res {}",tLeaveFormEntities);
        return tLeaveFormEntities;
    }


}
