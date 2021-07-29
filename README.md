# Honeybee

数据治理平台（Data governance platform）。

## 简介

#### 架构

#### 技术栈

- Java 1.8
- VUE 2.x 
- Spring boot 2.3.2.RELEASE
- Spring cloud alibaba 2.2.4.RELEASE
- Nacos
- MySQL 5.7
- Redis 3.x
- Spark 2.2.1
- Scala 2.11.8

#### 模块说明

- honeybee-cloud-data：通用数据服务。
- honeybee-cloud-gateway: 统一服务网关，支持熔断。
- honeybee-cloud-job：任务调度服务。
- honeybee-cloud-monitor：监控中心。
- honeybee-cloud-server：管理服务。
- honeybee-cloud-modules：微服务模块。
  - bees-quality：数据质量检查服务。
  - bees-connector：数据源连接器，基于标准的API实现对不同数据库的接入支持。