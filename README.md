# 情绪垃圾桶匿名墙（Java Web）

这是一个基于 **Java Web + JSP + Servlet + SQLite** 的匿名情绪墙：

- 登录：通过 `users` 用户表进行身份验证
- 增：发布带情绪标签的“纸条”
- 查：支持“时间轴”与“随机流”两种查看模式
- 删：仅允许撤回自己发布的纸条
- UI：采用 Apple 液态玻璃风格，按情绪字段显示不同背景色

## 1. 环境准备

- JDK 8+
- Maven 3.8+
- Tomcat 9+

## 2. 数据库初始化（SQLite）

1. 确保已配置 SQLite JDBC（项目内已通过 Maven 依赖引入）。
2. 按需修改数据库连接（推荐环境变量覆盖）：
   - `DB_URL`（示例：`jdbc:sqlite:/tmp/mood-wall.db`）
   - `DB_DRIVER`（默认：`org.sqlite.JDBC`）
3. 执行 SQL 脚本初始化表与示例数据：
   ```bash
   sqlite3 data/mood-wall.db < sql/schema.sql
   ```

## 3. 构建与部署

```bash
mvn clean package
```

生成 `target/java-web-crud.war`，部署到 Tomcat `webapps/` 后访问：

- `http://localhost:8080/java-web-crud/`

默认账号：

- 用户名：`admin`
- 密码：`123456`

## 4. 项目结构

- `src/main/java/com/example/servlet`：登录、退出、匿名墙控制器
- `src/main/java/com/example/dao`：用户与纸条数据访问
- `src/main/java/com/example/model`：纸条模型
- `src/main/java/com/example/util`：数据库连接与安全工具
- `src/main/webapp`：登录页与匿名墙页面
- `sql/schema.sql`：SQLite 初始化脚本
