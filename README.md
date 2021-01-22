# Honeybee

数据治理平台（Data governance platform）。

## 简介

#### 架构

#### 技术栈

- Java 1.8
- VUE 2.x
- Spring boot 2.2.0.RELEASE
- Spring cloud alibaba 2.2.0.RELEASE
- Nacos
- MySQL 5.7
- Redis 3.x
- Spark 2.2.1
- Scala 2.11.8

#### 模块说明

- data-server：提供通用数据服务的接口服务。
- web：前端界面。
- bees：数据质量检查服务。
- web-server：
  - connector：数据源连接器，基于标准的API实现对不同数据库的接入支持。
  - server：后端服务。