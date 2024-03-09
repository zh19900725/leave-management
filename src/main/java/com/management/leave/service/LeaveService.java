package com.management.leave.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    EmployeeServiceDao employeeServiceIDao;

    @Autowired
    LeaveFormServiceDao leaveFormServiceDao;


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
        LeaveFormEntity leaveForm = null;
        int flag = 0;
        ActionEnum action = ActionEnum.query(leaveRequestDTO.getAction());
        Assert.assertNotNull(ErrorInfo.ERROR_ACTION_NOT_SUPPORT, action);
        switch (action) {
            case EDIT:
                flag = edit(leaveRequestDTO,loginInfo);
                break;
            case CANCEL:
                flag = cancelOrDel(leaveRequestDTO.getFormId(),loginInfo.getUserName(),StatusEnum.CANCEL);
                break;
            case DELETE:
                flag = cancelOrDel(leaveRequestDTO.getFormId(),loginInfo.getUserName(),StatusEnum.CLOSE);
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
    public int edit(LeaveRequestDTO leaveRequestDTO,EmployeeInfo loginUser) {
        log.info("edit leave form, params: {}",leaveRequestDTO);
        int flag;
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(leaveRequestDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus()==0);
        // 只有申请人可以编辑请假单，其他人的编辑请求驳回
        Assert.assertTrue(ErrorInfo.ERROR_NO_PERMISSION,leaveForm.getApplicantId()==loginUser.getUserId());

        leaveForm.setUpdateTime(new Date());
        leaveForm.setReason(leaveRequestDTO.getReason());
        leaveForm.setStartTime(new Date(leaveRequestDTO.getStartTime()));
        leaveForm.setEndTime(new Date(leaveRequestDTO.getEndTime()));
        leaveForm.setCurOperator(loginUser.getUserName());
        flag = leaveFormServiceDao.getBaseMapper().updateById(leaveForm);
        log.info("edit leave form, result: {}",flag);
        return flag;
    }

    /**
     * 撤回或者删除请假单
     * @param formId 请假单id
     * @return
     */
    public int cancelOrDel(Integer formId, String operatorName,StatusEnum statusEnum) {
        log.info("change leave form status, params: formId={}",formId);
        int flag;
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(formId);
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus()==0);
        leaveForm.setStatus(statusEnum.getCode());
        leaveForm.setCurOperator(operatorName);
        leaveForm.setUpdateTime(new Date());
        if (StatusEnum.CLOSE.equals(statusEnum)) {
            leaveForm.setRowStatus(0);
        }
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
        LeaveFormEntity leaveForm = leaveFormServiceDao.getBaseMapper().selectById(approvalDTO.getFormId());
        Assert.assertNotNull(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm);
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_FORM_NOT_FOUND, leaveForm.getRowStatus()==0);

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
        EmployeeEntity employeeEntity = employeeServiceIDao.getBaseMapper().selectById(loginInfo.getUserId());
        Assert.assertNotNull(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity);
        Assert.assertTrue(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity.getRowStatus()==0);
        checkExist(leaveRequestDTO, employeeEntity.getId());

        // 请假单不存在，创建请假单
        LeaveFormEntity leaveFormEntity = new LeaveFormEntity();
        leaveFormEntity.setApplicantId(loginInfo.getUserId());
        leaveFormEntity.setCreateTime(new Date());
        leaveFormEntity.setUpdateTime(new Date());
        leaveFormEntity.setStatus(StatusEnum.WAITE_FIRST_CONFIRM.getCode());
        leaveFormEntity.setStartTime(new Date(leaveRequestDTO.getStartTime()));
        leaveFormEntity.setEndTime(new Date(leaveRequestDTO.getEndTime()));
        leaveFormEntity.setReason(leaveRequestDTO.getReason());
        leaveFormEntity.setFirstApprover(employeeEntity.getSuperiorId());
        leaveFormEntity.setCurOperator(loginInfo.getUserName());
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
        Assert.assertTrue(ErrorInfo.ERROR_LEAVE_IS_STILL_EXIST, list == null);
        return list;
    }


    /**
     * 查询请假单列表
     * @param req
     * @return
     */
    public List<LeaveFormEntity> getLeaveFormList(LeaveFormListDTO req){
        log.info("getLeaveFormList req {}",req);
        EmployeeEntity employeeEntity = employeeServiceIDao.getBaseMapper().selectById(req.getUserId());
        Assert.assertNotNull(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity);
        Assert.assertTrue(ErrorInfo.ERROR_USER_NOT_EXIST, employeeEntity.getRowStatus()==0);

        QueryWrapper<LeaveFormEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(employeeEntity.getEmployeeName())){
            wrapper.eq("employee_name", employeeEntity.getEmployeeName());
        }
        if ( req.getStartTime()>0){
            wrapper.ge("start_time", new Date(req.getStartTime()));
        }
        if ( req.getEndTime()>0){
            wrapper.le("end_time", new Date(req.getEndTime()));
        }
        if (null!=req.getStatus()){
            wrapper.le("status", req.getStatus());
        }
        wrapper.eq("row_status", "0");

        List<LeaveFormEntity> tLeaveFormEntities = leaveFormServiceDao.getBaseMapper().selectList(wrapper);
        log.debug("getLeaveFormList res {}",tLeaveFormEntities);
        return tLeaveFormEntities;
    }


}
