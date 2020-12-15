-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: honeybee
-- ------------------------------------------------------
-- Server version	5.7.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS honeybee;
CREATE USER 'honeybee'@'%' IDENTIFIED BY 'honeybee';
GRANT ALL ON honeybee.* to 'honeybee'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE honeybee;

--
-- Table structure for table `dgp_access_log`
--

DROP TABLE IF EXISTS `dgp_access_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_access_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `app_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_app主键ID ，请求用户',
  `dataservice_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_data_service 主键id',
  `request_params` text COMMENT '请求参数',
  `access_start_time` datetime DEFAULT NULL COMMENT '访问开始时间',
  `access_end_time` datetime DEFAULT NULL COMMENT '访问结束时间',
  `access_times` int(11) DEFAULT NULL COMMENT '访问耗时(s)',
  `db_start_time` datetime DEFAULT NULL COMMENT 'DB执行开始时间',
  `db_end_time` datetime DEFAULT NULL COMMENT 'DB执行结束时间',
  `exec_times` int(11) DEFAULT NULL COMMENT '执行耗时(s)',
  `instance_host` varchar(100) DEFAULT NULL COMMENT '实例IP',
  `instance_port` int(11) DEFAULT NULL COMMENT '端口',
  `service_source_id` varchar(100) DEFAULT NULL COMMENT '数据源取数:数据源ID、缓存取数:0、执行失败：留空',
  `resource_num` int(11) DEFAULT NULL COMMENT '持有资源数',
  `free_resource_num` int(11) DEFAULT NULL COMMENT '剩余资源数',
  `client_host` varchar(100) DEFAULT NULL COMMENT '客户端IP',
  `return_code` varchar(100) DEFAULT NULL COMMENT '返回状态码',
  `message` text COMMENT '返回信息',
  `return_row` int(11) DEFAULT NULL COMMENT '返回行数',
  `return_size` int(11) DEFAULT NULL COMMENT '返回数据量大小，单位kb',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='api访问日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_access_log`
--

LOCK TABLES `dgp_access_log` WRITE;
/*!40000 ALTER TABLE `dgp_access_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_access_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_app`
--

DROP TABLE IF EXISTS `dgp_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_app` (
  `id` varchar(100) NOT NULL COMMENT '主键，app用户编号，格式：APP+6位数字序列号 如：APP000001',
  `name` varchar(100) DEFAULT NULL COMMENT 'app用户名称',
  `app_desc` varchar(250) DEFAULT NULL COMMENT '描述',
  `owner` bigint(20) DEFAULT NULL COMMENT 'app责任人',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: active 有效 inactive 无效 deleted 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='APP用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_app`
--

LOCK TABLES `dgp_app` WRITE;
/*!40000 ALTER TABLE `dgp_app` DISABLE KEYS */;
INSERT INTO `dgp_app` VALUES ('APP_000002','Zhangsan','',6691952877096669184,'enabled','2020-07-23 06:32:36',1,'2020-07-23 06:32:36',NULL),('APP_000003','Lisi','',6691952877096669184,'enabled','2020-07-25 14:04:17',1,'2020-07-25 14:04:17',NULL);
/*!40000 ALTER TABLE `dgp_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_app_ds`
--

DROP TABLE IF EXISTS `dgp_app_ds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_app_ds` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `app_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_api_user主键ID',
  `dataservice_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_data_service 主键id',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，app_id+dataservice_id+seq_num唯一',
  `token` varchar(2000) DEFAULT NULL COMMENT 'token值，系统生成',
  `token_expired_time` datetime DEFAULT NULL COMMENT 'token 失效时间，为4999/01/01表示永不失效',
  `sample` char(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`app_id`,`dataservice_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='APP授权表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_app_ds`
--

LOCK TABLES `dgp_app_ds` WRITE;
/*!40000 ALTER TABLE `dgp_app_ds` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_app_ds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_app_ds_field`
--

DROP TABLE IF EXISTS `dgp_app_ds_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_app_ds_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `app_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_api_user主键ID',
  `dataservice_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_data_service 主键id',
  `field_id` varchar(100) DEFAULT NULL COMMENT '字段id',
  `field_name` varchar(100) DEFAULT NULL COMMENT '字段名称',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，app_id+dataservice_id+field_id+seq_num唯一',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`app_id`,`dataservice_id`,`field_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='APP授权字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_app_ds_field`
--

LOCK TABLES `dgp_app_ds_field` WRITE;
/*!40000 ALTER TABLE `dgp_app_ds_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_app_ds_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_audit_log`
--

DROP TABLE IF EXISTS `dgp_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_audit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `task_name` varchar(100) DEFAULT NULL COMMENT 'master-heartbeat/ master-resource/master-instance-stage/instance-heartbeat/instance-resource',
  `task_start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
  `task_end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `host` varchar(100) DEFAULT NULL COMMENT 'IP地址',
  `port` varchar(100) DEFAULT NULL COMMENT '端口',
  `thread` varchar(100) DEFAULT NULL COMMENT '进程号',
  `status_code` varchar(100) DEFAULT NULL COMMENT '返回状态码',
  `message` text COMMENT '返回信息',
  `context` text COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='平台审计日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_audit_log`
--

LOCK TABLES `dgp_audit_log` WRITE;
/*!40000 ALTER TABLE `dgp_audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_permission`
--

DROP TABLE IF EXISTS `dgp_auth_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_permission` (
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  `operation_id` bigint(20) DEFAULT NULL COMMENT '操作id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_permission`
--

LOCK TABLES `dgp_auth_permission` WRITE;
/*!40000 ALTER TABLE `dgp_auth_permission` DISABLE KEYS */;
INSERT INTO `dgp_auth_permission` VALUES (1,1,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,2,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,2,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,2,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,3,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,3,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,3,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,4,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,4,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,4,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,5,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,5,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,5,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,6,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,7,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,7,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,7,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,8,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,8,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,8,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,9,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,10,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,10,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,10,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,11,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,12,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,12,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,12,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,13,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,14,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,15,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,16,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,17,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,18,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,19,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,19,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,19,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,20,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,20,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,20,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,23,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,24,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,27,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,28,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,29,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,30,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,30,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,30,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,31,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(1,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,11,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,12,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,9,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,10,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,10,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,11,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,12,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,12,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,14,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,15,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,29,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,30,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,30,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,31,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,6,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,7,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,7,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,8,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,8,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,9,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,10,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,10,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,11,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,12,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,12,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,21,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,22,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,29,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,30,2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,30,3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,31,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,32,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(4,33,1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1);
/*!40000 ALTER TABLE `dgp_auth_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_resource`
--

DROP TABLE IF EXISTS `dgp_auth_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `code` varchar(100) DEFAULT NULL COMMENT '资源编码',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `descr` varchar(255) DEFAULT NULL COMMENT '资源描述',
  `parent_id` varchar(50) DEFAULT NULL COMMENT '父级资源id',
  `status` char(10) DEFAULT NULL COMMENT '状态 1启用，0禁用',
  `category_id` varchar(50) DEFAULT NULL COMMENT '分类id',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `attr1` varchar(255) DEFAULT NULL COMMENT '预留属性1',
  `attr2` varchar(255) DEFAULT NULL COMMENT '预留属性2',
  `attr3` varchar(255) DEFAULT NULL COMMENT '预留属性3',
  `attr4` varchar(255) DEFAULT NULL COMMENT '预留属性4',
  `attr5` varchar(255) DEFAULT NULL COMMENT '预留属性5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_resource`
--

LOCK TABLES `dgp_auth_resource` WRITE;
/*!40000 ALTER TABLE `dgp_auth_resource` DISABLE KEYS */;
INSERT INTO `dgp_auth_resource` VALUES (1,'permission','安全管理','安全管理','0','1','1',7,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'/permission','security','','',''),(2,'users','平台用户管理','平台用户管理','1','1','2',71,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'users','circle','','',''),(3,'resources','平台资源管理','平台资源管理','1','1','2',73,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'resources','circle','','',''),(4,'roles','平台角色管理','平台角色管理','1','1','2',72,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'roles','circle','','',''),(5,'serviceuser','服务用户管理','服务用户管理','1','1','2',74,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'serviceuser','circle','','',''),(6,'assetmanage','数据资产管理','数据资产管理','0','1','1',2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'/assetmanage','datasource02','','',''),(7,'assetclassifi','资产领域管理','资产领域管理','6','1','2',21,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'assetclassifi','circle','','',''),(8,'assetheme','资产主题管理','资产主题管理','6','1','2',22,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'assetheme','circle','','',''),(9,'serversource','数据源管理','数据源管理','0','1','1',4,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','asset','','',''),(10,'datasource','数据源管理','数据源管理','9','1','2',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'datasource','asset','','',''),(11,'serviceapi','数据服务管理','数据服务管理','0','1','1',1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','service02','','',''),(12,'servicemanage','数据服务管理','数据服务管理','11','1','2',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'servicemanage','service02','','',''),(13,'operationmoni','运维监控','运维监控','0','1','1',5,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'/operationmoni','monitor','','',''),(14,'statemonitor','数据源监控','数据源监控','13','1','1',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'statemonitor','circle','','',''),(15,'accessstatic','数据服务监控','数据服务监控','13','1','1',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'accessstatic','circle','','',''),(16,'logshow','服务访问日志','服务访问日志','13','1','1',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'logshow','circle','','',''),(17,'platform','平台管理','平台管理','0','1','1',6,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'/platform','setting','','',''),(18,'cloudserver','缓存管理','缓存管理','17','1','1',63,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'cloudserver','circle','','',''),(19,'drivemane','服务驱动管理','服务驱动管理','17','1','2',61,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'drivemane','circle','','',''),(20,'clusteropera','服务运维管理','服务运维管理','17','1','2',62,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'clusteropera','circle','','',''),(21,'test',' 首页','首页','0','1','1',8,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','','','true',''),(22,'home','首页','首页','21','1','1',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'home','','','',''),(23,'paramConfig','参数配置','参数配置','0','1','1',9,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','paramconfig','','',''),(24,'systemparam','系统参数配置','系统参数配置','23','1','1',NULL,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'systemparam','paramconfig','','',''),(27,'clusteraudit','服务审计日志','服务审计日志','13','1','1',0,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'clusteraudit','circle','','',''),(28,'runlog','平台操作日志','平台操作日志','13','1','1',0,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'runlog','circle','','',''),(29,'model','服务模型管理','服务模型管理','0','1','1',3,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','','','',''),(30,'modelma','服务模型管理','服务模型管理','29','1','2',0,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'modelma','model02','','',''),(31,'assetmap02','数据资产地图','数据资产地图','6','1','1',24,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'dataassetmap','circle','','',''),(32,'messageFirst','消息中心','消息中心','0','1','1',0,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'','','','true',''),(33,'message','消息中心','消息中心','32','1','1',0,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1,'message','','','true','');
/*!40000 ALTER TABLE `dgp_auth_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_resource_category`
--

DROP TABLE IF EXISTS `dgp_auth_resource_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_resource_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `code` varchar(100) DEFAULT NULL COMMENT '分类编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `descr` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='资源类别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_resource_category`
--

LOCK TABLES `dgp_auth_resource_category` WRITE;
/*!40000 ALTER TABLE `dgp_auth_resource_category` DISABLE KEYS */;
INSERT INTO `dgp_auth_resource_category` VALUES (1,'menu','菜单','菜单','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1),(2,'button','按钮','按钮','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1);
/*!40000 ALTER TABLE `dgp_auth_resource_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_resource_operation`
--

DROP TABLE IF EXISTS `dgp_auth_resource_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_resource_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `code` varchar(50) DEFAULT NULL COMMENT '操作编号',
  `name` varchar(150) DEFAULT NULL COMMENT '名称',
  `descr` varchar(255) DEFAULT NULL COMMENT '操作描述',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='资源操作';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_resource_operation`
--

LOCK TABLES `dgp_auth_resource_operation` WRITE;
/*!40000 ALTER TABLE `dgp_auth_resource_operation` DISABLE KEYS */;
INSERT INTO `dgp_auth_resource_operation` VALUES (1,'access','访问','访问',1,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(2,'access','访问','访问',2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1),(3,'operation','操作','操作',2,'2020-07-23 05:08:01',1,'2020-07-23 05:08:01',1);
/*!40000 ALTER TABLE `dgp_auth_resource_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_role`
--

DROP TABLE IF EXISTS `dgp_auth_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `code` varchar(50) DEFAULT NULL COMMENT '操作编号',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `status` varchar(5) DEFAULT NULL COMMENT '状态 1启用，0禁用',
  `descr` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_role`
--

LOCK TABLES `dgp_auth_role` WRITE;
/*!40000 ALTER TABLE `dgp_auth_role` DISABLE KEYS */;
INSERT INTO `dgp_auth_role` VALUES (1,'administrator','平台管理员','1','','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1),(2,'data_app','数据应用用户','1','','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1),(3,'data_engineer','数据工程师','1','','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1),(4,'data_architect','数据架构师','1','','2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1);
/*!40000 ALTER TABLE `dgp_auth_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_user`
--

DROP TABLE IF EXISTS `dgp_auth_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `login_name` varchar(255) DEFAULT NULL COMMENT '登入名',
  `password` varchar(500) DEFAULT NULL COMMENT '密码',
  `passwd_salt` varchar(500) DEFAULT NULL COMMENT '密码盐',
  `last_passwd_change` datetime DEFAULT NULL COMMENT '上一次修改密码时间',
  `previous_login` datetime DEFAULT NULL COMMENT '上一次登陆时间',
  `name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `sex` varchar(5) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(20) DEFAULT NULL COMMENT '移动电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` varchar(5) DEFAULT NULL COMMENT '状态',
  `user_source` varchar(255) DEFAULT NULL COMMENT '用户来自',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `ldap_user` varchar(20) DEFAULT NULL COMMENT 'ldap',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6691952877096669185 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_user`
--

LOCK TABLES `dgp_auth_user` WRITE;
/*!40000 ALTER TABLE `dgp_auth_user` DISABLE KEYS */;
INSERT INTO `dgp_auth_user` VALUES (1,'admin','XnHnwdwziUe8g16Nkv3KjEE+e3qQP7/n4L7CSCgGUK4=','admin','2020-07-10 14:42:09','2020-07-10 14:42:09','Will','男','1500000000','will@emil.com','1','','2020-07-10 14:42:09',0,'2020-07-10 14:42:09',0,''),(6691952877096669184,'test','bbC91kd7S9V/XZJwwfywm+aPxpEhOVZEkwkKd5ejGoA=','27775693257e4bbd9ad6fd801f6ef6ca','2020-07-23 00:00:00','2020-07-23 00:00:00','Test','2','','','1','NaN','2020-07-23 00:00:00',1,'2020-07-23 00:00:00',NULL,NULL);
/*!40000 ALTER TABLE `dgp_auth_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_auth_user_role`
--

DROP TABLE IF EXISTS `dgp_auth_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_auth_user_role` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_auth_user_role`
--

LOCK TABLES `dgp_auth_user_role` WRITE;
/*!40000 ALTER TABLE `dgp_auth_user_role` DISABLE KEYS */;
INSERT INTO `dgp_auth_user_role` VALUES (1,1,'2020-07-23 05:08:00',1,'2020-07-23 05:08:00',1),(6691952877096669184,2,'2020-07-23 14:31:36',1,NULL,NULL);
/*!40000 ALTER TABLE `dgp_auth_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_code`
--

DROP TABLE IF EXISTS `dgp_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `type` varchar(100) DEFAULT NULL COMMENT '快码',
  `code` varchar(100) DEFAULT NULL COMMENT '快码',
  `display_name` varchar(100) DEFAULT NULL COMMENT '快码显示名称',
  `parent_type` varchar(100) DEFAULT NULL COMMENT '父类型',
  `parent_code` varchar(100) DEFAULT NULL COMMENT '父类型快码',
  `code_desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8 COMMENT='快码表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_code`
--

LOCK TABLES `dgp_code` WRITE;
/*!40000 ALTER TABLE `dgp_code` DISABLE KEYS */;
INSERT INTO `dgp_code` VALUES (1,'mode_type','asset','资产模型','','','模型类型，资产模型编码','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(2,'mode_type','ability','能力模型','','','模型类型，能力模型编码','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(3,'ability_type','component','组件','mode_type','ability','能力模型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(4,'ability_type','solution','解决方案','mode_type','ability','能力模型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(5,'ability_type','API','应用接口','mode_type','ability','能力模型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(6,'component_type','key_value','key_value 存储','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(7,'component_type','object','object 存储','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(8,'component_type','doc','doc 存储','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(9,'component_type','stream','流计算','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(10,'component_type','visualization','可视化','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(11,'component_type','MQ','消息队列','ability_type','component','能力模型组件类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(12,'solution_type','ASR','语音识别','ability_type','solution','能力模型解决方案类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(13,'solution_type','image recognition','图像识别','ability_type','solution','能力模型解决方案类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(14,'solution_type','text parsing','文本解析','ability_type','solution','能力模型解决方案类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(15,'solution_type','forecast','预测','ability_type','solution','能力模型解决方案类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(16,'api_type','DAQ','数据采集','ability_type','API','能力模型应用接口类型分类','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(17,'service_type','asset','资产服务','','','服务类型，资产服务编码','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(18,'service_type','ability','能力服务','','','服务类型，能力服务编码','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(19,'driver_status_code','enabled','启动','','','服务驱动状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(20,'driver_status_code','disabled','禁用','','','服务驱动状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(21,'source_status_code','enabled','启用','','','数据源状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(22,'source_status_code','disabled','禁用','','','数据源状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(23,'source_stage_code','unpublished','待发布','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(24,'source_stage_code','publishing','发布中','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(25,'source_stage_code','published','已发布','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(26,'source_stage_code','waiting','待下线','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(27,'source_stage_code','stopping','下线中','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(28,'source_stage_code','offline','已下线','','','数据源阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(29,'area_status_code','enabled','启动','','','资产领域状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(30,'topic_status_code','enabled','启动','','','资产主题状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(31,'model_status_code','published','已发布','','','服务模型状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(32,'model_status_code','initialized','待发布','','','服务模型状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(33,'service_status_code','enabled','启动','','','数据服务状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(34,'service_status_code','disabled','禁用','','','数据服务状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(35,'app_status_code','enabled','启动','','','APP用户状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(36,'app_status_code','disabled','禁用','','','APP用户状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(37,'instance_status_code','unknown','未知','','','实例集群状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(38,'instance_status_code','normal','正常','','','实例集群状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(39,'instance_status_code','stopping','待停止','','','实例集群状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(40,'instance_status_code','stopped','停止','','','实例集群状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(41,'instance_stage_code','unactivated','未激活','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(42,'instance_stage_code','activated','已激活','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(43,'instance_stage_code','initialized','已初始化','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(44,'instance_stage_code','registered','已注册','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(45,'instance_stage_code','online','已上线','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(46,'instance_stage_code','unregister','取消注册','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(47,'instance_stage_code','dealloc','释放资源','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(48,'instance_stage_code','stopped','停止','','','实例集群阶段','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(49,'resource_type_code','connection','数据源连接','','','资源类型','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(50,'message_level_code','notice','公告','','','消息中心','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(51,'message_level_code','warning','警告','','','消息中心','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(52,'message_status_code','read','已读','','','消息中心','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(53,'message_status_code','unread','未读','','','消息中心','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(54,'auditLog_status_code','200','成功','','','服务审计日志状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(55,'auditLog_status_code','500','失败','','','服务审计日志状态','2020-07-23 05:08:01',0,'2020-07-23 05:08:01',0),(56,'auditLog_task_code','masterHeartbeat','管理-心跳线程','','','服务审计日志任务','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(57,'auditLog_task_code','masterResource','管理-资源线程','','','服务审计日志任务','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(58,'auditLog_task_code','instanceHeartbeat','实例-心跳线程','','','服务审计日志任务','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(59,'auditLog_task_code','instanceResource','实例-资源线程','','','服务审计日志任务','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(60,'sysauditLog_status_code','200','成功','','','平台操作日志状态','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(61,'sysauditLog_status_code','500','失败','','','平台操作日志状态','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(62,'config_type','cluster','集群','','','cluster，参数类型大类','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(63,'config_type','gateway','网关','','','gateway，参数类型大类','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(64,'cluster_sub_type','instance','实例','config_type','cluster','cluster，参数类型cluster子类','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(65,'cluster_sub_type','common','公用','config_type','cluster','cluster，参数类型cluster子类','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0),(66,'gateway_sub_type','nginx','Nginx','config_type','gateway','cluster，参数类型gateway子类','2020-07-23 05:08:02',0,'2020-07-23 05:08:02',0);
/*!40000 ALTER TABLE `dgp_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_data_assets_area`
--

DROP TABLE IF EXISTS `dgp_data_assets_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_data_assets_area` (
  `id` varchar(100) NOT NULL COMMENT '主键，数据模型分类ID,格式：DMA+6位数字序列号 如：DMA000001',
  `area_name` varchar(100) DEFAULT NULL COMMENT '数据资产分类名称',
  `area_desc` varchar(250) DEFAULT NULL COMMENT '数据资产分类描述',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: enabled 启用 deleted 删除',
  `pic_path` varchar(250) DEFAULT NULL COMMENT '图标路径',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据资产领域';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_data_assets_area`
--

LOCK TABLES `dgp_data_assets_area` WRITE;
/*!40000 ALTER TABLE `dgp_data_assets_area` DISABLE KEYS */;
INSERT INTO `dgp_data_assets_area` VALUES ('DMA000006','数据共享','','enabled','dataSharing.png',NULL,NULL,NULL,NULL),('DMA000007','技术平台','','enabled','tecPlatForm.png',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `dgp_data_assets_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_data_assets_topic`
--

DROP TABLE IF EXISTS `dgp_data_assets_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_data_assets_topic` (
  `id` varchar(100) NOT NULL COMMENT '主键，数据模型主题ID,格式：DMT+6位数字序列号 如：DMT000001 ',
  `area_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_asset_type主键ID',
  `topic_name` varchar(100) DEFAULT NULL COMMENT '数据资产主题名称',
  `topic_desc` varchar(250) DEFAULT NULL COMMENT '数据资产主题描述',
  `owner` bigint(20) DEFAULT NULL COMMENT '主题责任人',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: enabled 启用 deleted 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据资产主题';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_data_assets_topic`
--

LOCK TABLES `dgp_data_assets_topic` WRITE;
/*!40000 ALTER TABLE `dgp_data_assets_topic` DISABLE KEYS */;
INSERT INTO `dgp_data_assets_topic` VALUES ('DMT_000002','DMA000006','测试主题','',NULL,'enabled','2020-07-23 05:36:24',1,'2020-07-23 05:36:24',NULL);
/*!40000 ALTER TABLE `dgp_data_assets_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_data_service`
--

DROP TABLE IF EXISTS `dgp_data_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_data_service` (
  `id` varchar(100) NOT NULL COMMENT '唯一，api编号,格式：API+6位数字序列号 如：API000001 ',
  `service_model_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_service_model主键ID',
  `type_code` varchar(100) DEFAULT NULL COMMENT '服务类型：data 数据服务 message 消息服务 api api服务',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: active 有效 inactive 无效 deleted 删除',
  `data_service_name` varchar(100) DEFAULT NULL COMMENT '数据服务名称',
  `operate_type` varchar(100) DEFAULT NULL COMMENT '操作类型，read 读 write 写，默认 read',
  `data_service_desc` varchar(250) DEFAULT NULL COMMENT '数据服务描述',
  `expression` text COMMENT 'sql表达式、表名、topic名,URL',
  `request_method` varchar(100) DEFAULT NULL COMMENT '请求方法：POST/GET',
  `body_pattern` text COMMENT 'http请求 body 模板样例',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_data_service`
--

LOCK TABLES `dgp_data_service` WRITE;
/*!40000 ALTER TABLE `dgp_data_service` DISABLE KEYS */;
INSERT INTO `dgp_data_service` VALUES ('API_000002','DM_000002','asset','deleted','数据共享-测试主题-测试数据模型','read',NULL,'select  order_no, channel2, channel3, dealer, city, store_no, store_name, store_type, customer, phone, sku_name, updatetime, amount, count from trade where 1 = 1  <#if order_no??> and order_no = ${order_no} </#if>  <#if channel2??> and channel2 = ${channel2} </#if> ','POST',NULL,'2020-07-23 06:27:48',1,'2020-08-06 03:50:27',1);
/*!40000 ALTER TABLE `dgp_data_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_instance`
--

DROP TABLE IF EXISTS `dgp_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_instance` (
  `id` varchar(100) NOT NULL COMMENT '主键，实例编号，格式：INS+6位数字序列号，如：INS000001',
  `name` varchar(100) DEFAULT NULL COMMENT '实例名称',
  `host` varchar(100) DEFAULT NULL COMMENT '实例主机IP',
  `host_user` varchar(100) DEFAULT NULL COMMENT '主机用户',
  `host_password` varchar(100) DEFAULT NULL COMMENT '主机密码',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `instance_path` varchar(100) DEFAULT NULL COMMENT '存放路径',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态 (unknown 未知 normal 正常 stopping 待停止 stopped 停止)',
  `stage_code` varchar(100) DEFAULT NULL COMMENT '阶段 (1.unactivated 未激活、2.activated 已激活、3.initialized 已初始化、4.registered 已注册、5.online 已上线、6.unregister 取消注册、7.dealloc  释放资源、8.stopped 停止)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`host`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集群实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_instance`
--

LOCK TABLES `dgp_instance` WRITE;
/*!40000 ALTER TABLE `dgp_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_instance_resource`
--

DROP TABLE IF EXISTS `dgp_instance_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_instance_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `instance_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_instance 主键ID',
  `service_source_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_service_source主键ID',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，instance_id+service_source_id+seq_num唯一',
  `resource_type_code` varchar(100) DEFAULT NULL COMMENT '资源类型 0 数据源',
  `expect_number` int(11) DEFAULT NULL COMMENT '预期资源数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`instance_id`,`service_source_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='实例资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_instance_resource`
--

LOCK TABLES `dgp_instance_resource` WRITE;
/*!40000 ALTER TABLE `dgp_instance_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_instance_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_message`
--

DROP TABLE IF EXISTS `dgp_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `message_level` varchar(100) DEFAULT NULL COMMENT '消息级别：notice 公共、warning 警告',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `context` text COMMENT '内容',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息中心表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_message`
--

LOCK TABLES `dgp_message` WRITE;
/*!40000 ALTER TABLE `dgp_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_message_object`
--

DROP TABLE IF EXISTS `dgp_message_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_message_object` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `message_id` bigint(20) DEFAULT NULL COMMENT '消息ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态：read 已读、unread 未读、deleted 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息对象';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_message_object`
--

LOCK TABLES `dgp_message_object` WRITE;
/*!40000 ALTER TABLE `dgp_message_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_message_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_monitor_kpi_appuser_day`
--

DROP TABLE IF EXISTS `dgp_monitor_kpi_appuser_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_monitor_kpi_appuser_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `access_time` varchar(100) DEFAULT NULL COMMENT '访问时间，格式：YYYY-MM-DD',
  `app_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '执行状态，成功/失败',
  `total_access_num` bigint(20) DEFAULT NULL COMMENT '累计访问次数',
  `total_access_time` bigint(20) DEFAULT NULL COMMENT '累计访问时长，单位：秒',
  `total_execute_time` bigint(20) DEFAULT NULL COMMENT '累计执行时长，单位：秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`access_time`,`app_id`,`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户访问每天监控指标统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_monitor_kpi_appuser_day`
--

LOCK TABLES `dgp_monitor_kpi_appuser_day` WRITE;
/*!40000 ALTER TABLE `dgp_monitor_kpi_appuser_day` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_monitor_kpi_appuser_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_monitor_kpi_appuser_hour`
--

DROP TABLE IF EXISTS `dgp_monitor_kpi_appuser_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_monitor_kpi_appuser_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `access_time` varchar(100) DEFAULT NULL COMMENT '访问时间，格式：YYYY-MM-DD HH24',
  `app_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '执行状态，成功/失败',
  `total_access_num` bigint(20) DEFAULT NULL COMMENT '累计访问次数',
  `total_access_time` bigint(20) DEFAULT NULL COMMENT '累计访问时长，单位：秒',
  `total_execute_time` bigint(20) DEFAULT NULL COMMENT '累计执行时长，单位：秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`access_time`,`app_id`,`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户访问每小时监控指标统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_monitor_kpi_appuser_hour`
--

LOCK TABLES `dgp_monitor_kpi_appuser_hour` WRITE;
/*!40000 ALTER TABLE `dgp_monitor_kpi_appuser_hour` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_monitor_kpi_appuser_hour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_monitor_kpi_dataservice_day`
--

DROP TABLE IF EXISTS `dgp_monitor_kpi_dataservice_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_monitor_kpi_dataservice_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `access_time` varchar(100) DEFAULT NULL COMMENT '访问时间，格式：YYYY-MM-DD',
  `dataservice_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '执行状态，成功/失败',
  `total_access_num` bigint(20) DEFAULT NULL COMMENT '累计访问次数',
  `total_access_time` bigint(20) DEFAULT NULL COMMENT '累计访问时长，单位：秒',
  `total_execute_time` bigint(20) DEFAULT NULL COMMENT '累计执行时长，单位：秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`access_time`,`dataservice_id`,`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务被访问每天监控指标统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_monitor_kpi_dataservice_day`
--

LOCK TABLES `dgp_monitor_kpi_dataservice_day` WRITE;
/*!40000 ALTER TABLE `dgp_monitor_kpi_dataservice_day` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_monitor_kpi_dataservice_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_monitor_kpi_dataservice_hour`
--

DROP TABLE IF EXISTS `dgp_monitor_kpi_dataservice_hour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_monitor_kpi_dataservice_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `access_time` varchar(100) DEFAULT NULL COMMENT '访问时间，格式：YYYY-MM-DD HH24',
  `dataservice_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '执行状态，成功/失败',
  `total_access_num` bigint(20) DEFAULT NULL COMMENT '累计访问次数',
  `total_access_time` bigint(20) DEFAULT NULL COMMENT '累计访问时长，单位：秒',
  `total_execute_time` bigint(20) DEFAULT NULL COMMENT '累计执行时长，单位：秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`access_time`,`dataservice_id`,`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务被访问每小时监控指标统计';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_monitor_kpi_dataservice_hour`
--

LOCK TABLES `dgp_monitor_kpi_dataservice_hour` WRITE;
/*!40000 ALTER TABLE `dgp_monitor_kpi_dataservice_hour` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_monitor_kpi_dataservice_hour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_parameter`
--

DROP TABLE IF EXISTS `dgp_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rel_object_type` varchar(100) DEFAULT NULL COMMENT '参数实体类型：driver 数据驱动，datasource 数据源',
  `rel_object_id` varchar(100) DEFAULT NULL COMMENT '实体ID,关联数据驱动/数据源ID',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，rel_object_type+rel_object_id+seq_num唯一',
  `param_name` varchar(100) DEFAULT NULL COMMENT '参数名称',
  `param_value` varchar(2000) DEFAULT NULL COMMENT '参数值',
  `display_name` varchar(100) DEFAULT NULL COMMENT '参数显示名称',
  `param_desc` varchar(250) DEFAULT NULL COMMENT '参数描述',
  `check_regexp` varchar(250) DEFAULT NULL COMMENT '参数校验正则表达式',
  `is_required` varchar(1) DEFAULT NULL COMMENT '是否必填',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_object_type` (`rel_object_type`,`rel_object_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_parameter`
--

LOCK TABLES `dgp_parameter` WRITE;
/*!40000 ALTER TABLE `dgp_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_service_driver`
--

DROP TABLE IF EXISTS `dgp_service_driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_service_driver` (
  `id` varchar(100) NOT NULL COMMENT '主键，驱动ID,格式：DRI+6位数字序列号，如：DRI000001',
  `driver_name` varchar(100) DEFAULT NULL COMMENT '驱动名称',
  `version` varchar(100) DEFAULT NULL COMMENT '驱动版本',
  `driver_desc` varchar(250) DEFAULT NULL COMMENT '描述',
  `driver_path` varchar(250) DEFAULT NULL COMMENT '驱动包路径',
  `driver_class` varchar(200) DEFAULT NULL COMMENT '主类入口',
  `status_code` varchar(50) DEFAULT NULL COMMENT '状态: enabled 启用 disabled 禁用 deleted 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务驱动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_service_driver`
--

LOCK TABLES `dgp_service_driver` WRITE;
/*!40000 ALTER TABLE `dgp_service_driver` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_service_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_service_model`
--

DROP TABLE IF EXISTS `dgp_service_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_service_model` (
  `id` varchar(100) NOT NULL COMMENT '主键，数据模型编号,格式：DM+6位数字序列号 如：DM000001 ',
  `model_name` varchar(100) DEFAULT NULL COMMENT '模型名称',
  `model_desc` varchar(250) DEFAULT NULL COMMENT '数据模型描述',
  `type_code` varchar(100) DEFAULT NULL COMMENT '模型类型：data 数据模型 message 消息模型 api api模型',
  `topic_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_data_model_topic主键ID',
  `service_source_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_service_source主键ID',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: published 已发布 initialized 代发布 deleted 删除',
  `request_method` varchar(100) DEFAULT NULL COMMENT '请求方法：POST/GET,针对模型类型是api模型',
  `expression_type` varchar(100) DEFAULT NULL COMMENT '数据模型内容获取类型：sql/table/topic/url',
  `expression` text COMMENT 'sql表达式、表名、topic名,URL',
  `cache_duration` int(11) DEFAULT NULL COMMENT '缓存时间，秒，0代表不缓存',
  `body_pattern` text COMMENT 'http请求 body 模板样例',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `parent_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据模型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_service_model`
--

LOCK TABLES `dgp_service_model` WRITE;
/*!40000 ALTER TABLE `dgp_service_model` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_service_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_service_model_field`
--

DROP TABLE IF EXISTS `dgp_service_model_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_service_model_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `service_model_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_service_model主键ID',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，datamodel_id+seq_num唯一',
  `field_name` varchar(100) DEFAULT NULL COMMENT '字段名称',
  `data_type` varchar(100) DEFAULT NULL COMMENT '字段类型',
  `expression` varchar(2000) DEFAULT NULL COMMENT '字段表达式',
  `field_desc` varchar(250) DEFAULT NULL COMMENT '字段描述',
  `is_derived` varchar(1) DEFAULT NULL COMMENT '是否是派生字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`service_model_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='数据模型字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_service_model_field`
--

LOCK TABLES `dgp_service_model_field` WRITE;
/*!40000 ALTER TABLE `dgp_service_model_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_service_model_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_service_model_param`
--

DROP TABLE IF EXISTS `dgp_service_model_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_service_model_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `service_model_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_service_model主键ID',
  `seq_num` int(11) DEFAULT NULL COMMENT '数字序列，datamodel_id+seq_num唯一',
  `param_name` varchar(100) DEFAULT NULL COMMENT '参数名称，header参数content_type为必选',
  `param_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `operate_type` varchar(100) DEFAULT NULL COMMENT '操作类型(大于、小于、等于)',
  `field_name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `default_value` text COMMENT '参数值(可设置默认值)',
  `param_desc` text COMMENT '描述',
  `param_type_code` varchar(100) DEFAULT NULL COMMENT '参数所在位置，仅对api模型有效, header/body, 其中header参数content_type为必选',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_01` (`service_model_id`,`seq_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据模型参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_service_model_param`
--

LOCK TABLES `dgp_service_model_param` WRITE;
/*!40000 ALTER TABLE `dgp_service_model_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_service_model_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_service_source`
--

DROP TABLE IF EXISTS `dgp_service_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_service_source` (
  `id` varchar(100) NOT NULL COMMENT '主键，数据源ID,格式：DTS+6位数字序列号，如：DTS000001',
  `driver_id` varchar(100) DEFAULT NULL COMMENT '关联dgp_driver的主键ID',
  `service_source_name` varchar(100) DEFAULT NULL COMMENT '数据源名称',
  `service_source_desc` varchar(250) DEFAULT NULL COMMENT '数据源描述',
  `status_code` varchar(100) DEFAULT NULL COMMENT '状态: enabled 启用 disabled 禁用 deleted 删除',
  `stage_code` varchar(100) DEFAULT NULL COMMENT '阶段 (1.unpublished 待发布 ，2.publishing 发布中，3.published 已发布，4.waiting 待下线，5.stopping 下线中，6.offline 已下线)',
  `max_connections` int(11) DEFAULT NULL COMMENT '最大连接数',
  `query_timeout` int(11) DEFAULT NULL COMMENT '查询超时时间',
  `conn_timeout` int(11) DEFAULT NULL COMMENT '连接超时时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_service_source`
--

LOCK TABLES `dgp_service_source` WRITE;
/*!40000 ALTER TABLE `dgp_service_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_service_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_sys_audit_log`
--

DROP TABLE IF EXISTS `dgp_sys_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_sys_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '访问IP',
  `status` varchar(250) DEFAULT NULL COMMENT '操作状态. 200 成功, 400失败 …',
  `api` varchar(250) DEFAULT NULL COMMENT '操作接口',
  `api_desc` varchar(250) DEFAULT NULL COMMENT '操作接口描述',
  `request_time` datetime DEFAULT NULL COMMENT '访问时间',
  `response_time` mediumtext COMMENT '响应时间（单位：毫秒）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='平台操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_sys_audit_log`
--

LOCK TABLES `dgp_sys_audit_log` WRITE;
/*!40000 ALTER TABLE `dgp_sys_audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_sys_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_sys_config`
--

DROP TABLE IF EXISTS `dgp_sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `config_type` varchar(200) DEFAULT NULL COMMENT '参数大类',
  `config_subtype` varchar(200) DEFAULT NULL COMMENT '参数子类',
  `config_name` varchar(200) DEFAULT NULL COMMENT '系统参数',
  `default_value` varchar(200) DEFAULT NULL COMMENT '参数默认值',
  `config_value` varchar(200) DEFAULT NULL COMMENT '系统参数值',
  `display_name` varchar(201) DEFAULT NULL COMMENT '显示名称',
  `config_desc` varchar(202) DEFAULT NULL COMMENT '描述',
  `readonly` char(1) DEFAULT NULL COMMENT '是否只读',
  `code_type` varchar(100) DEFAULT NULL COMMENT '快码所属类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_sys_config`
--

LOCK TABLES `dgp_sys_config` WRITE;
/*!40000 ALTER TABLE `dgp_sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `dgp_sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dgp_sys_sequence`
--

DROP TABLE IF EXISTS `dgp_sys_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_sys_sequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `table_name` varchar(50) DEFAULT NULL COMMENT '来源表',
  `seq_prefix` varchar(50) DEFAULT NULL COMMENT 'ID前缀',
  `seq_desc` varchar(50) DEFAULT NULL COMMENT 'ID描述',
  `seq_digit` int(11) DEFAULT NULL COMMENT '序列号位数',
  `last_use_seq` int(11) DEFAULT NULL COMMENT '最后使用序列号',
  `seq_increment` int(11) DEFAULT NULL COMMENT '自增度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='自增序列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dgp_sys_sequence`
--

LOCK TABLES `dgp_sys_sequence` WRITE;
/*!40000 ALTER TABLE `dgp_sys_sequence` DISABLE KEYS */;
INSERT INTO `dgp_sys_sequence` VALUES (1,'SEQ_EXAMPLE','EG_','序列测试',6,1,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(2,'dgp_service_source','DTS_','数据源',6,3,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(3,'dgp_data_service','API_','数据服务',6,2,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(4,'app_user','APP_','应用用户',6,3,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(5,'dgp_data_assets_area','DMA_','资产领域',6,1,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(6,'dgp_data_assets_topic','DMT_','资产主题',6,2,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(7,'dgp_service_model','DM_','资产模型',6,2,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(8,'dgp_service_driver','DRI_','数据驱动',6,2,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1),(9,'instance','INS_','集群实例',6,9,1,'2020-07-23 05:08:02',1,'2020-07-23 05:08:02',1);
/*!40000 ALTER TABLE `dgp_sys_sequence` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

DROP TABLE IF EXISTS `dgp_sys_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dgp_sys_sequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `table_name` varchar(50) DEFAULT NULL COMMENT '来源表',
  `seq_prefix` varchar(50) DEFAULT NULL COMMENT 'ID前缀',
  `seq_desc` varchar(50) DEFAULT NULL COMMENT 'ID描述',
  `seq_digit` int(11) DEFAULT NULL COMMENT '序列号位数',
  `last_use_seq` int(11) DEFAULT NULL COMMENT '最后使用序列号',
  `seq_increment` int(11) DEFAULT NULL COMMENT '自增度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='自增序列表';


DELIMITER $$
-- get_organization_tree
DROP FUNCTION IF EXISTS `get_organization_tree`$$
CREATE FUNCTION `get_organization_tree`(rootId BIGINT) RETURNS VARCHAR(1000) CHARSET utf8
BEGIN
		 DECLARE sTemp VARCHAR(1000);
     DECLARE sTempChd VARCHAR(1000);
				 SET sTemp = '$';
				 SET sTempChd = CAST(rootId AS CHAR);
				 WHILE sTempChd IS NOT NULL DO
         SET sTemp = CONCAT(sTemp,',',sTempChd);
         SELECT GROUP_CONCAT(id) INTO sTempChd FROM auth_organization WHERE FIND_IN_SET(PARENT_ID,sTempChd)>0;
     END WHILE;
		 RETURN sTemp;
END$$
-- seq_currval
DROP FUNCTION IF EXISTS `seq_currval`$$
CREATE FUNCTION `seq_currval`(v_seq_name VARCHAR(50)) RETURNS VARCHAR(50) CHARSET utf8
BEGIN
    DECLARE VALUE VARCHAR(50);
    SET VALUE = '0';
    SELECT CONCAT(seq_prefix, LPAD(last_use_seq, seq_digit, '0')) INTO VALUE FROM dgp_sys_sequence WHERE table_name = v_seq_name;
		RETURN VALUE;
END$$

-- seq_nextval
DROP FUNCTION IF EXISTS `seq_nextval`$$
CREATE FUNCTION `seq_nextval`(v_seq_name VARCHAR(50)) RETURNS VARCHAR(50) CHARSET utf8
BEGIN
    UPDATE dgp_sys_sequence SET last_use_seq = last_use_seq + seq_increment  WHERE table_name = v_seq_name;
    RETURN seq_currval(v_seq_name);
END$$

-- seq_nextval
DROP FUNCTION IF EXISTS `del_access_log`$$
CREATE PROCEDURE `del_access_log`(in s int)
BEGIN
      DELETE FROM dgp_access_log WHERE create_time<DATE_ADD(CURDATE() ,INTERVAL -s DAY);
END$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-06 10:21:31
