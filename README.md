# JAVA WEB 实验：B/S 模式数据库访问

本项目实现了一个基于 **Java Web + Tomcat + MySQL** 的实验示例：
- 用户表（用户名、密码）登录验证
- 登录后进入 Web 应用
- 对 `student_records` 表执行增、删、改、查（CRUD）操作

## 1. 环境准备

- JDK 8+
- Maven 3.8+
- MySQL 8+
- Tomcat 9+

## 2. 数据库安装与配置（MySQL）

1. 安装并启动 MySQL。
2. 执行脚本初始化数据库、表和数据：
   ```sql
   source /绝对路径/java/sql/schema.sql;
   ```
3. 修改数据库连接配置文件：
   - 文件：`src/main/resources/db.properties`
   - 重点修改：`db.url`、`db.username`、`db.password`

## 3. Java 数据库访问环境配置

项目通过 Maven 引入 JDBC 驱动：
- `mysql:mysql-connector-java:8.0.33`

数据库连接工具类：
- `src/main/java/com/example/util/DbUtil.java`

## 4. Tomcat 安装与部署

1. 安装 Tomcat 并确认可访问 `http://localhost:8080`。
2. 在项目根目录打包：
   ```bash
   mvn clean package
   ```
3. 将生成的 `target/java-web-crud.war` 部署到 Tomcat 的 `webapps/` 目录。
4. 启动 Tomcat。

## 5. 运行与验证

浏览器访问：
- `http://localhost:8080/java-web-crud/`

默认登录账号：
- 用户名：`admin`
- 密码：`123456`

登录后可对学生信息表进行：
- 新增
- 查询（列表展示）
- 修改
- 删除

## 6. 项目结构

- `src/main/java/com/example/servlet`：登录、退出、CRUD 控制器
- `src/main/java/com/example/dao`：数据库访问逻辑
- `src/main/java/com/example/model`：实体类
- `src/main/webapp`：JSP 页面
- `sql/schema.sql`：数据库脚本
