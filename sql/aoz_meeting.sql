/*
 Navicat Premium Data Transfer

 Source Server         : aoz_meeting
 Source Server Type    : MySQL
 Source Server Version : 50650 (5.6.50-log)
 Source Host           : www.ly1024.top:3306
 Source Schema         : aoz_meeting

 Target Server Type    : MySQL
 Target Server Version : 50650 (5.6.50-log)
 File Encoding         : 65001

 Date: 14/08/2023 15:38:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` bigint(20) NOT NULL COMMENT '部门ID',
  `department_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `department_sort_id` int(11) NULL DEFAULT NULL,
  `is_deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`department_id`) USING BTREE,
  INDEX `departmentId`(`department_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for meet_project
-- ----------------------------
DROP TABLE IF EXISTS `meet_project`;
CREATE TABLE `meet_project`  (
  `meet_id` bigint(20) NOT NULL,
  `meet_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议标题',
  `room_id` bigint(20) NOT NULL COMMENT '需要使用的会议室ID',
  `date_time` date NOT NULL COMMENT '会议日期\n会议日期',
  `start_time_integer` int(11) NOT NULL COMMENT '会议开始时间(半小时为1单位,如00:00则为0)',
  `end_time_integer` int(11) NOT NULL COMMENT '会议结束时间(半小时为1单位,如00:30则为1)',
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '会议室使用申请人ID',
  `meet_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会议描述',
  `meet_status` int(11) NOT NULL DEFAULT 0 COMMENT '会议室申请状态(0表示审批中，1表示同意，2表示拒绝，3表示取消)',
  `create_time` datetime NOT NULL COMMENT '提交申请的世界',
  `update_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `approval_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批意见',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人姓名',
  `is_deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`meet_id`) USING BTREE,
  INDEX `roomId`(`room_id`) USING BTREE,
  INDEX `applicantId`(`user_id`) USING BTREE,
  CONSTRAINT `meet_project_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `meeting_room` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `meet_project_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for meeting_device
-- ----------------------------
DROP TABLE IF EXISTS `meeting_device`;
CREATE TABLE `meeting_device`  (
  `device_id` bigint(20) NOT NULL COMMENT '会议设备ID',
  `device_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议设备名称:例如投影仪麦克风',
  `is_deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for meeting_room
-- ----------------------------
DROP TABLE IF EXISTS `meeting_room`;
CREATE TABLE `meeting_room`  (
  `room_id` bigint(20) NOT NULL COMMENT '会议室ID',
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议室名称',
  `user_num` int(11) NOT NULL COMMENT '该会议室可容纳的人数',
  `is_deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`room_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for room_device
-- ----------------------------
DROP TABLE IF EXISTS `room_device`;
CREATE TABLE `room_device`  (
  `id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,
  `device_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `roomId`(`room_id`) USING BTREE,
  INDEX `devieceId`(`device_id`) USING BTREE,
  CONSTRAINT `room_device_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `meeting_room` (`room_id`) ON DELETE NO ACTION ON UPDATE RESTRICT,
  CONSTRAINT `room_device_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `meeting_device` (`device_id`) ON DELETE NO ACTION ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号，登陆默认使用手机号作为账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department_id` bigint(20) NOT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '用户类型：1表示管理员，空表示用户',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '用户是否被禁用：0表示正常，1表示被禁用',
  `gid` bigint(20) NULL DEFAULT NULL COMMENT '有度中的用户ID',
  `is_deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE COMMENT '手机号唯一性',
  INDEX `user_ibfk_1`(`department_id`) USING BTREE COMMENT '外键约束',
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
