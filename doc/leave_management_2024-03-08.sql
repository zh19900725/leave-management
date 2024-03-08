# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 9.135.223.65 (MySQL 5.7.18-txsql-log)
# Database: leave_management
# Generation Time: 2024-03-08 11:37:08 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table t_employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_employee`;

CREATE TABLE `t_employee` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(50) DEFAULT '' COMMENT '姓名',
  `role` int(11) DEFAULT '1' COMMENT '角色id,1普通职员，2主管，3高级主管',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `row_status` int(11) DEFAULT '0' COMMENT '状态，0正常，1删除',
  `superior_id` int(11) DEFAULT NULL COMMENT '上司',
  `mobile` varchar(15) NOT NULL DEFAULT '' COMMENT '系统登录密码',
  `login_name` varchar(20) NOT NULL DEFAULT '' COMMENT '系统登录名',
  `email` varchar(50) DEFAULT '' COMMENT '员工邮箱',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mobile` (`mobile`),
  KEY `idx_employee_name` (`employee_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_employee` WRITE;
/*!40000 ALTER TABLE `t_employee` DISABLE KEYS */;

INSERT INTO `t_employee` (`id`, `employee_name`, `role`, `create_time`, `row_status`, `superior_id`, `mobile`, `login_name`, `email`)
VALUES
	(1,'aarronzhang',1,'2024-03-08 16:44:13',0,2,'17791928991','aarronzhang','469977171@qq.com'),
	(2,'lisi',1,'2024-03-08 16:45:38',0,3,'15389406776','lisi','123456@qq.com'),
	(3,'wangwu',1,'2024-03-08 16:46:24',0,0,'17791928990','wangwu','469977171@qq.com');

/*!40000 ALTER TABLE `t_employee` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table t_leave_form
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_leave_form`;

CREATE TABLE `t_leave_form` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `applicant_Id` varchar(20) DEFAULT NULL COMMENT '申请人，对应员工表的员工id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请假单创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请假单更新时间',
  `status` varchar(15) DEFAULT 'FirstConfirm' COMMENT '请假单状态，默认为待1级确认',
  `start_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请假开始时间',
  `end_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请假结束时间',
  `reason` varchar(100) DEFAULT '' COMMENT '请假原因',
  `first_approver` int(11) DEFAULT '0' COMMENT '一级审批人',
  `second_approver` int(11) NOT NULL DEFAULT '0' COMMENT '二级审批人',
  `first_comment` varchar(100) DEFAULT '' COMMENT '一级审批意见',
  `second_comment` varchar(100) NOT NULL DEFAULT '' COMMENT '二级审批意见',
  `cur_operator` varchar(11) DEFAULT '' COMMENT '最后更新申请单的操作人信息',
  `row_status` int(11) DEFAULT '0' COMMENT '状态0正常，1删除',
  PRIMARY KEY (`id`),
  KEY `idx_applicantId` (`applicant_Id`),
  KEY `idx_status` (`status`),
  KEY `idx_first_approver` (`first_approver`),
  KEY `idx_second_approver` (`second_approver`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_leave_form` WRITE;
/*!40000 ALTER TABLE `t_leave_form` DISABLE KEYS */;

INSERT INTO `t_leave_form` (`id`, `applicant_Id`, `create_time`, `update_time`, `status`, `start_time`, `end_time`, `reason`, `first_approver`, `second_approver`, `first_comment`, `second_comment`, `cur_operator`, `row_status`)
VALUES
	(1,'1','2024-03-08 09:21:05','2024-03-08 10:14:04','FirstConfirm','2024-03-08 08:53:23','2024-03-08 08:36:43','i want rest',2,0,'','','aarronzhang',-1),
	(2,'1','2024-03-08 09:31:16','2024-03-08 10:16:38','FirstConfirm','2024-03-08 08:53:23','2024-03-08 08:36:43','i want rest',2,0,'','','aarronzhang',0),
	(3,'1','2024-03-08 09:34:59','2024-03-08 09:34:59','FirstConfirm','2024-03-08 08:53:23','2024-03-08 08:36:43','i want rest',2,0,'','','aarronzhang',0),
	(4,'1','2024-03-08 09:37:50','2024-03-08 09:37:50','FirstConfirm','2024-03-08 08:53:23','2024-03-08 08:36:43','i want rest',2,0,'','','aarronzhang',0);

/*!40000 ALTER TABLE `t_leave_form` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
