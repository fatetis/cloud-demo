## nacos服务注册和配置中心（docker部署）

### 添加依赖
```
<!-- Nacos 服务发现 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<!-- Nacos 配置中心 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>${spring-cloud-nacos-config.version}</version>
</dependency>
```
### 数据库配置
```
1、创建数据库nacos_config
2、执行sql文件,初始化数据库，文件路径/environment/mysql-schema.sql
```
### 环境参数：
镜像：nacos/nacos-server:v2.4.3  
SPRING_DATASOURCE_PLATFORM：mysql持久化保存  
MYSQL_SERVICE_HOST：数据库连接地址  
MYSQL_SERVICE_PORT：数据库端口号  
MYSQL_SERVICE_DB_NAME：数据库名称  
MYSQL_SERVICE_USER：数据库登录账号   
MYSQL_SERVICE_PASSWORD：数据库登录密码  
NACOS_AUTH_USERNAME：nacos登录账号  
NACOS_AUTH_PASSWORD：nacos登录密码  
NACOS_AUTH_ENABLE： nacos开启登录授权  
NACOS_AUTH_TOKEN：nacos授权token  
NACOS_AUTH_IDENTITY_KEY:nacos授权key  
NACOS_AUTH_IDENTITY_VALUE： nacos授权value  

### docker部署示例：
```
docker run -d --name nacos --restart=always -p 8848:8848 -p 9848:9848 -p 9849:9849 -e MODE=standalone -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=192.168.50.103 -e MYSQL_SERVICE_PORT=3306 -e MYSQL_SERVICE_DB_NAME=nacos_config -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=123456 -e NACOS_AUTH_USERNAME=nacos -e NACOS_AUTH_PASSWORD=nacos -e NACOS_AUTH_ENABLE=true -e NACOS_AUTH_TOKEN=U2VjQXBwTmFjb3NUb2tlbjEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA== -e NACOS_AUTH_IDENTITY_KEY=ServerSecurityKey20260430 -e NACOS_AUTH_IDENTITY_VALUE=ServerSecurityValue20260430abc -v 挂载的目录路径:/home/nacos/logs nacos/nacos-server:v2.4.3
```
### 项目application.yml配置
```
spring:
  application:
    name: cloud-order
  profiles:
    active: dev
  cloud:
    nacos:
      username: nacos
      password: nacos
      # nacos配置中心
      config:
        server-addr: 127.0.0.1:8848
        group: DEFAULT_GROUP
        file-extension: yaml
        namespace: public
        username: ${spring.cloud.nacos.username}
        password: ${spring.cloud.nacos.password}
      # nacos服务注册发现
      discovery:
        server-addr: 127.0.0.1:8848
        username: ${spring.cloud.nacos.username}
        password: ${spring.cloud.nacos.password}
  #引入nacos配置
  config:
    import: nacos:${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```
### nacos控制台创建配置
1、登录 Nacos → 配置管理 → 配置列表  
2、点击 + 新建配置  
3、填写：  
3.1）Data ID：user-service-dev.yaml
3.2）规则：${服务名}-${环境}.${格式}  
3.3）Group：DEFAULT_GROUP  
3.4）配置格式：YAML  
3.5）配置内容： yaml
#### 示例配置
```
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cloud_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```
点击 发布