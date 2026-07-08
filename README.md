# springBoot3+springCloudAlibaba企业级组件 


## 软件说明
基于springBoot3+springCloudAlibaba框架融合各个企业生产环境具备组件：  
1、✅️服务治理：Nacos + Gateway  
2、数据一致性：Seata  
3、可观测性：SkyWalking + Prometheus + Loki  
4、安全：OAuth2 / Keycloak  
5、异步：消息队列  
6、分布式能力：Redis、分布式任务  
7、容器化部署：Docker / K8s

注：打钩✅是当前版本包含的组件，未打钩是还在开发中ing
## 端口放行

* 外部访问端口  
    通讯网关:8088 
* 内部通讯端口   
  nacos服务：8848、9848、9849


## 运行环境
+ springBoot 3
+ springCloudAlibaba 2023
+ MySQL8.0
+ java 17+
+ maven3.9.9

## 项目架构
```
cloud-demo/
├── cloud-common(公共模块)       # 所有服务共享：JWT工具、实体、常量、异常
├── cloud-gateway(网关模块)      # 全局请求拦截、JWT校验、路由转发
├── cloud-auth(认证中心)         # 登录、JWT生成、用户认证（唯一授权入口）
├── cloud-order(业务服务)        # 无权限代码，仅处理业务
└── cloud-user(业务服务)         # 无权限代码，仅处理业务

```
