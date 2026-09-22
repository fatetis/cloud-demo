# 阶段1：构建层，maven3.8.8 + jdk17
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

RUN echo '<?xml version="1.0" encoding="UTF-8"?> \
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0" \
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd"> \
  <localRepository>/build/maven_repo</localRepository> \
  <mirrors> \
    <mirror> \
      <id>aliyunmaven</id> \
      <mirrorOf>central</mirrorOf> \
      <name>阿里云公共仓库</name> \
      <url>https://maven.aliyun.com/repository/public</url> \
    </mirror> \
  </mirrors> \
</settings>' > /usr/share/maven/conf/settings.xml

# 构建参数，切换微服务只需要改这个
ARG SERVICE_MODULE=cloud-gateway
ARG JAR_FILE=${SERVICE_MODULE}/target/${SERVICE_MODULE}*.jar

# 第一步：只复制所有pom文件，缓存依赖
COPY pom.xml .
COPY cloud-common/pom.xml ./cloud-common/
COPY cloud-common/cloud-common-api/pom.xml ./cloud-common/cloud-common-api/
COPY cloud-common/cloud-common-core/pom.xml ./cloud-common/cloud-common-core/
COPY cloud-common/cloud-common-mybatis/pom.xml ./cloud-common/cloud-common-mybatis/
COPY cloud-common/cloud-common-security/pom.xml ./cloud-common/cloud-common-security/
COPY cloud-common/cloud-common-web/pom.xml ./cloud-common/cloud-common-web/
COPY cloud-gateway/pom.xml ./cloud-gateway/
COPY cloud-auth/pom.xml ./cloud-auth/
COPY cloud-user/pom.xml ./cloud-user/
COPY cloud-order/pom.xml ./cloud-order/

# 预下载依赖
RUN mvn dependency:go-offline -pl ${SERVICE_MODULE} -am

COPY . .
RUN mvn clean package -DskipTests -pl ${SERVICE_MODULE} -am

# 使用通配符获取jar，自动适配带版本号jar包
ARG JAR_FILE=${SERVICE_MODULE}/target/*.jar
RUN ls -la ${JAR_FILE}
RUN java -Djarmode=layertools -jar ${JAR_FILE} extract --destination /build/extracted
RUN ls -la /build/extracted

# 阶段2：运行层，alpine jre17 轻量镜像
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 按依赖变更频率，从最稳定到最易变依次拷贝（Docker缓存顺序至关重要）
# 1. 第三方稳定依赖（几乎不会变，缓存命中率最高）
COPY --from=builder /build/extracted/dependencies/ ./
# 2. spring boot loader包
COPY --from=builder /build/extracted/spring-boot-loader/ ./
# 3. snapshot依赖（公共模块cloud-common如果是SNAPSHOT在这里）
COPY --from=builder /build/extracted/snapshot-dependencies/ ./
# 4. 业务代码（只有业务修改才会触发这一层重建）
COPY --from=builder /build/extracted/application/ ./

ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=70.0","org.springframework.boot.loader.launch.JarLauncher"]
