# Java 微服务基础脚手架（无业务逻辑，开源可二次开发）
```angular2html
说明：纯净基础微服务框架，不含任何业务代码，仅完成微服务基础架构搭建，可直接基于本源码进行业务二次开发。
```
## 基础运行环境
+ springBoot 3.2.5
+ springCloudAlibaba 2023.0.1
+ MySQL8.0
+ java21
+ maven3.9.9

## 技术栈
### 基于springBoot3.2.5+springCloudAlibaba2023.0.1框架融合各个组件：  
1、✅️服务治理：Nacos + Gateway  
2、数据一致性：Seata  
3、可观测性：SkyWalking + Prometheus + Loki  
4、安全：OAuth2 / Keycloak  
5、异步：消息队列  
6、分布式能力：Redis、分布式任务  
7、容器化部署：Docker / K8s  
8、OpenFeign：服务远程调用  
注：打钩✅是当前版本包含的组件，未打钩是还在开发中ing

## 端口放行
* 外部访问端口  
    通讯网关:8088 
* 内部通讯端口   
  nacos服务：8848、9848、9849


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

## 源代码要点示例（关键片段）
### 1、父 pom.xml（版本统一管控）
```angular2html
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>cloud-demo</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>

    <name>cloud-demo</name>
    <description>Spring Cloud 微服务父工程</description>

    <modules>
        <module>cloud-common</module>
        <module>cloud-auth</module>
        <module>cloud-api</module>
        <module>cloud-gateway</module>
        <module>cloud-user</module>
        <module>cloud-order</module>
    </modules>

    <properties>
        <!--项目版本-->
        <revision>0.0.1-SNAPSHOT</revision>

        <!--依赖版本-->
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.5</spring-boot.version>
        <spring-cloud.version>2023.0.1</spring-cloud.version>
        <spring-cloud-alibaba.version>2023.0.1.0</spring-cloud-alibaba.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <spring-cloud-nacos-config.version>2023.0.3.2</spring-cloud-nacos-config.version>
        <jjwt.version>0.11.5</jjwt.version>
        <hutool-all.version>5.8.25</hutool-all.version>
        <cloud-commons.version>1.0.0</cloud-commons.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- 微服务公共模块 -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- 微服务公共模块-授权 -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common-security</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- 微服务公共模块-核心 -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common-core</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- 微服务公共模块-API -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common-api</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- 微服务公共模块-数据库 -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common-mybatis</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- 微服务公共模块-WEB -->
            <dependency>
                <groupId>com.example.commons</groupId>
                <artifactId>cloud-common-web</artifactId>
                <version>${cloud-commons.version}</version>
                <scope>compile</scope>
            </dependency>
            <!-- Spring Boot -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Spring Cloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Spring Cloud Alibaba -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- MyBatis Plus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>
            <!-- Nacos 注册中心 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
                <version>${spring-cloud-nacos-config.version}</version>
            </dependency>
            <!-- JJWT JWT工具包（支持SpringBoot3） -->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <!-- 公用工具 -->
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>${hutool-all.version}</version>
            </dependency>

        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
        </plugins>
    </build>
</project>

```

## 未完待续，努力搭建中
若觉得有用的话，求关注求收藏，您的关注和收藏是我持续搭建开发的动力
