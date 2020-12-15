# Honeybee

数据治理平台（Data governance platform）。

## 简介

#### 架构

#### 技术栈

- JAVA 1.8
- VUE 2.x
- Spring boot 2.2.0.RELEASE
- Spring cloud alibaba 2.2.0.RELEASE
- Redis 3.x
- Nacos
- MySQL 5.7

#### 模块说明

- api-server：提供通用数据服务的接口服务。
- honeybee-web：前端界面。
- server-parent：后台服务，按功能模块进行拆分。
  - server-auth： 用户身份认证。
  - server-auth-shiro：用户权限认证。
  - server-dataasset：数据资产管理。
  - server-dataservice：数据服务管理。
  - server-datasource：数据源管理。
  - server-operations：监控审计管理。
  - server-param：系统参数管理。
  - server-platform：平台管理。
  - server-system：系统管理。
- core：核心业务模块。
- entity：全局实体对象模块。
- connector：数据源连接器，基于标准的API实现对不同数据库的接入支持。

## 快速开始

#### Run on Docker
> TODO

#### 构建
> TODO

#### 启动

1.启动Nacos服务实例。

2.启动Honeybee web。

3.启动Honeybee api server。
