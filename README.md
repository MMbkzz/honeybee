# Honeybee

数据治理平台（Data governance platform）。

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
- - core：核心业务模块。
- - entity：全局实体对象模块。
- connector：数据源连接器，基于标准的API实现对不同数据库的接入支持。

#### 构建


#### 启动

1.启动 Nacos服务实例。

2.启动Honeybee web。

3.启动Honeybee api server。
