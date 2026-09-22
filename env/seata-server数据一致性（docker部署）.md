# 采用nacos注册中心，db存储

## 一、新建数据库seata_server 且 新建表

### 建表脚本

```angular2html
-- -------------------------------- The script used when storeMode is 'db' --------------------------------
-- the table to store GlobalSession data
CREATE TABLE IF NOT EXISTS `global_table`
(
`xid`                       VARCHAR(128) NOT NULL,
`transaction_id`            BIGINT,
`status`                    TINYINT      NOT NULL,
`application_id`            VARCHAR(32),
`transaction_service_group` VARCHAR(32),
`transaction_name`          VARCHAR(128),
`timeout`                   INT,
`begin_time`                BIGINT,
`application_data`          VARCHAR(2000),
`gmt_create`                DATETIME,
`gmt_modified`              DATETIME,
PRIMARY KEY (`xid`),
KEY `idx_status_gmt_modified` (`status` , `gmt_modified`),
KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
`branch_id`         BIGINT       NOT NULL,
`xid`               VARCHAR(128) NOT NULL,
`transaction_id`    BIGINT,
`resource_group_id` VARCHAR(32),
`resource_id`       VARCHAR(256),
`branch_type`       VARCHAR(8),
`status`            TINYINT,
`client_id`         VARCHAR(64),
`application_data`  VARCHAR(2000),
`gmt_create`        DATETIME(6),
`gmt_modified`      DATETIME(6),
PRIMARY KEY (`branch_id`),
KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
`row_key`        VARCHAR(128) NOT NULL,
`xid`            VARCHAR(128),
`transaction_id` BIGINT,
`branch_id`      BIGINT       NOT NULL,
`resource_id`    VARCHAR(256),
`table_name`     VARCHAR(32),
`pk`             VARCHAR(36),
`status`         TINYINT      NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
`gmt_create`     DATETIME,
`gmt_modified`   DATETIME,
PRIMARY KEY (`row_key`),
KEY `idx_status` (`status`),
KEY `idx_branch_id` (`branch_id`),
KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `distributed_lock`
(
`lock_key`       CHAR(20) NOT NULL,
`lock_value`     VARCHAR(20) NOT NULL,
`expire`         BIGINT,
primary key (`lock_key`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4;

INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);
```
## 二、新建seata-server的application.yml配置文件
路径配置：./seata-server/application.yml

## 三、准备nacos配置中心配置

DATA-ID:seataServer.yaml  
Group:SEATA_GROUP

参考../seata-server.yaml文件配置

## 四、配置MYSQL
> my.cnf 永久配置
```

[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
init-connect='SET NAMES utf8mb4'
max_connections=1000
server-id=1
log-bin=mysql-bin
wait_timeout = 3600
interactive_timeout = 3600
max_allowed_packet = 64M
max_connections = 500
```

## 五、docker拉取镜像并创建容器
> 镜像：apache/seata-server:2.1.0
## 六、温馨提示
连接mysql8.0版本以上需挂载mysql-connector-java-8.0.30.jar驱动到镜像文件夹（/seata-server/libs/）

## 七、docker部署命令
```angular2html
docker run -d --name seata-server -p 7091:7091 -p 8091:8091 -e SEATA_IP=宿主机IP -e SEATA_PORT=8091 -v /替换成第二步创建application.yml的路径/application.yml:/seata-server/resources/application.yml -v /替换成日志挂载路径/logs:/root/logs/seata --restart always apache/seata-server:2.1.0
```

