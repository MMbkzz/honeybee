CREATE DATABASE IF NOT EXISTS `honeybee` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE USER 'honeybee'@'localhost' IDENTIFIED BY 'honeybee';
GRANT ALL ON honeybee.* to 'honeybee'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE honeybee;

-- honeybee.hb_account definition

CREATE TABLE `hb_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_name` varchar(50) NOT NULL COMMENT '账户名称',
  `account_password` varchar(50) NOT NULL COMMENT '账户密码',
  `account_role` bigint(20) NOT NULL COMMENT '账户角色ID',
  `account_realname` varchar(50) DEFAULT NULL COMMENT '姓名',
  `account_gender` int(2) DEFAULT NULL COMMENT '性别（0/1）',
  `account_email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `account_phone` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='平台账户表';


-- honeybee.hb_assets_catalog definition

CREATE TABLE `hb_assets_catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `isroot` int(2) NOT NULL COMMENT '根节点标示（0/1）',
  `catalog_name` varchar(50) NOT NULL COMMENT '目录名称',
  `catalog_code` varchar(50) NOT NULL COMMENT '目录编码',
  `catalog_parent_id` bigint(20) NOT NULL COMMENT '所属父级ID',
  `catalog_type` varchar(50) NOT NULL COMMENT '目录类型（domain/topic）',
  `catalog_order` int(2) NOT NULL COMMENT '序号',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据资产目录表';


-- honeybee.hb_assets_model definition

CREATE TABLE `hb_assets_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `assets_model_name` varchar(50) NOT NULL COMMENT '数据资产模型名称',
  `assets_model_code` varchar(50) NOT NULL COMMENT '数据资产模型编号',
  `assets_catalog_domain` bigint(20) NOT NULL COMMENT '数据资产域',
  `assets_catalog_topic` bigint(20) NOT NULL COMMENT '数据资产主题',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源ID',
  `datasource_meta` text NOT NULL COMMENT '元数据配置（JSON）',
  `expression` text NOT NULL COMMENT '表达式',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据资产模型表';


-- honeybee.hb_audit_log definition

CREATE TABLE `hb_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `log_title` varchar(50) NOT NULL COMMENT '日志标题',
  `log_audit` varchar(50) NOT NULL COMMENT '审计类型',
  `log_type` varchar(50) NOT NULL COMMENT '日志类型',
  `log_content` text NOT NULL COMMENT '日志内容',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审计日志表';


-- honeybee.hb_data_recycler definition

CREATE TABLE `hb_data_recycler` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `assets_model_id` bigint(20) NOT NULL COMMENT '数据资产模型ID',
  `assets_data_size` bigint(20) NOT NULL COMMENT '数据量（KB）',
  `assets_data_count` bigint(20) NOT NULL COMMENT '数据条目数量',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据回收站表';


-- honeybee.hb_data_service definition

CREATE TABLE `hb_data_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_service_name` varchar(50) NOT NULL COMMENT '数据服务名称',
  `data_service_code` varchar(50) NOT NULL COMMENT '数据服务编号',
  `assets_model_id` bigint(20) NOT NULL COMMENT '数据资产模型ID',
  `datasource_meta` text NOT NULL COMMENT '元数据配置（JSON）',
  `service_meta` text NOT NULL COMMENT '参数配置（JSON）',
  `cache_expire` int(50) NOT NULL COMMENT '缓存过期时间（ms）',
  `expression` text NOT NULL COMMENT '表达式',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务配置表';


-- honeybee.hb_data_service_authority definition

CREATE TABLE `hb_data_service_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `data_service_id` bigint(20) NOT NULL COMMENT '数据服务ID',
  `authority_token` varchar(50) NOT NULL COMMENT '服务授权码',
  `authority_expire` bigint(20) NOT NULL COMMENT '服务授权有效期（timestamp）',
  `authority_data` text NOT NULL COMMENT '数据权限配置（JSON）',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务授权表';


-- honeybee.hb_data_service_endpoint definition

CREATE TABLE `hb_data_service_endpoint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_service_endpoint_code` varchar(50) NOT NULL COMMENT '接口编码',
  `service_node_id` bigint(20) NOT NULL COMMENT '数据服务节点ID',
  `data_service_id` bigint(20) NOT NULL COMMENT '数据服务配置ID',
  `data_service_resource` int(10) NOT NULL COMMENT '资源分配（DB CONN）',
  `data_service_endpoint` varchar(200) NOT NULL COMMENT '服务URL',
  `data_service_status` varchar(50) NOT NULL COMMENT '接口状态',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务接口表';


-- honeybee.hb_data_service_node definition

CREATE TABLE `hb_data_service_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `service_node_name` varchar(50) NOT NULL COMMENT '节点名称',
  `service_node_code` varchar(50) NOT NULL COMMENT '节点编码',
  `service_node_ip` varchar(50) NOT NULL COMMENT '节点IP',
  `service_node_port` varchar(50) NOT NULL COMMENT '节点端口',
  `service_node_endpoint` varchar(200) NOT NULL COMMENT '节点URL',
  `service_node_status` varchar(50) NOT NULL COMMENT '节点状态',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务节点表';


-- honeybee.hb_data_service_tenant definition

CREATE TABLE `hb_data_service_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_name` varchar(50) NOT NULL COMMENT '租户名称',
  `tenant_code` varchar(50) NOT NULL COMMENT '租户编码',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据服务租户表';


-- honeybee.hb_datasource definition

CREATE TABLE `hb_datasource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `datasource_name` varchar(50) NOT NULL COMMENT '数据源名称',
  `datasource_code` varchar(50) NOT NULL COMMENT '数据源编码',
  `datasource_config` text NOT NULL COMMENT '数据源参数配置（JSON）',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源参数配置表';


-- honeybee.hb_quality_job definition

CREATE TABLE `hb_quality_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `job_name` varchar(50) NOT NULL COMMENT '任务名称',
  `job_code` varchar(50) NOT NULL COMMENT '任务编号（唯一）',
  `job_expression` varchar(50) NOT NULL COMMENT '任务规则（调度策略）',
  `job_order` int(2) NOT NULL COMMENT '优先级',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据巡检任务表';


-- honeybee.hb_quality_rule definition

CREATE TABLE `hb_quality_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_name` varchar(50) NOT NULL COMMENT '规则名称',
  `rule_code` varchar(50) NOT NULL COMMENT '规则编号',
  `rule_type` varchar(50) NOT NULL COMMENT '规则类型',
  `rule_config_yaml` text NOT NULL COMMENT '完整配置（JSON）',
  `job_id` bigint(20) NOT NULL COMMENT '任务调度ID',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据巡检规则表';


-- honeybee.hb_quality_rule_config definition

CREATE TABLE `hb_quality_rule_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) NOT NULL COMMENT '规则ID',
  `rule_config_type` varchar(50) NOT NULL COMMENT '参数类型',
  `rule_config_key` varchar(100) NOT NULL COMMENT '参数名',
  `rule_config_value` text NOT NULL COMMENT '参数值',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据巡检规则配置参数表';


-- honeybee.hb_sys_config definition

CREATE TABLE `hb_sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置项名称',
  `config_value` text NOT NULL COMMENT '配置项内容',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';


-- honeybee.hb_sys_message definition

CREATE TABLE `hb_sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `message_title` varchar(50) NOT NULL COMMENT '消息标题',
  `message_type` varchar(50) NOT NULL COMMENT '消息类型',
  `message_content` varchar(200) NOT NULL COMMENT '消息内容',
  `message_receiver` bigint(20) NOT NULL COMMENT '收件人ID',
  `status` int(2) NOT NULL COMMENT '状态',
  `owner` bigint(20) NOT NULL COMMENT '操作人',
  `updatetime` datetime NOT NULL COMMENT '记录更新时间',
  `createtime` datetime NOT NULL COMMENT '记录创建时间',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统消息通知表';