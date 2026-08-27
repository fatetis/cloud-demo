# Java 微服务基础脚手架（无业务逻辑，开源可二次开发）
```angular2html
说明：纯净基础微服务框架，不含任何业务代码，仅完成微服务基础架构搭建，可直接基于本源码进行业务二次开发。
```
## 基础运行环境
+ springBoot 3.2.5
+ springCloudAlibaba 2023.0.1
+ MySQL8.0
+ java17+
+ maven3.9.9

## 技术栈
### 基于springBoot3.2.5+springCloudAlibaba2023.0.1框架融合各个组件：  
1、✅️服务治理：Nacos-server:2.4.3 + Gateway  
2、✅️数据一致性：Seata-server:2.6.0  
3、可观测性：SkyWalking + Prometheus + Loki  
4、安全：OAuth2 / Keycloak  
5、异步：消息队列  
6、分布式能力：Redis、分布式任务  
7、容器化部署：Docker / K8s  
8、✅OpenFeign：服务远程调用  
9、✅️数据库连接池druid
注：打钩✅是当前版本包含的组件，未打钩是还在开发中ing

## 端口放行
* 外部访问端口  
    通讯网关:8088 
* 内部通讯端口   
  nacos服务：8848、9848、9849
  seata-server服务：7091、8091


## 模块划分（纯净脚手架，无业务）
```
cloud-demo/
├── cloud-common(公共模块)       # 所有服务共享：JWT工具、实体、常量、异常
|   ├── cloud-common-api        # 对外API层：Feign接口、DTO、VO、枚举。只放接口和数据模型，尽量轻，零业务逻辑
|   ├── cloud-common-core       # 核心通用：工具类、统一返回、全局异常、#AOP、过滤器、Web配置、Jackson序列化、日期处理
|   ├── cloud-common-mybatis    # ORM层：Mybatis‑plus配置、分页插件、基础BaseEntity、公共Mapper父类
|   ├── cloud-common-security   # 权限、JWT、token解析、安全过滤器
|   ├── cloud-common-web        # WEB层
├── cloud-gateway(网关模块)      # 全局请求拦截、JWT校验、路由转发
├── cloud-auth(认证中心)         # 登录、JWT生成、用户认证（唯一授权入口）
├── cloud-order(业务服务)        # 示例业务微服务模块（无业务逻辑，仅演示CRUD基础模板）
└── cloud-user(业务服务)         # 示例业务微服务模块（无业务逻辑，仅演示CRUD基础模板）
```

## 未完待续，努力搭建中
若觉得有用的话，求关注求收藏，您的关注和收藏是我持续搭建开发的动力
