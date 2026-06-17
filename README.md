# 个人财务管理系统

基于 Spring Boot 3、MyBatis-Plus、MySQL 的个人财务管理系统，包含完整后端接口和内置 Web UI。

## 功能

- 收入 / 支出流水管理
- 自定义分类
- 按月份和分类查询流水
- 月度总收入、总支出、结余统计
- 分类支出统计
- 每日支出趋势
- 月度预算新增、编辑、删除
- 超支检查
- Knife4j / OpenAPI 接口文档

## 技术栈

- Java 17+
- Spring Boot 3.1.5
- MyBatis-Plus 3.5.3.1
- MySQL 8+
- Hutool
- Knife4j
- Lombok
- HTML / CSS / JavaScript

## 快速开始

### 1. 创建数据库

执行初始化脚本：

```bash
mysql -uroot -p --default-character-set=utf8mb4 < sql/init.sql
```

脚本会创建 `finance` 数据库以及以下表：

- `transaction`
- `budget`
- `category_dict`

### 2. 配置数据库连接

默认连接配置：

```yaml
MYSQL_URL=jdbc:mysql://localhost:3306/finance?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
MYSQL_USERNAME=root
MYSQL_PASSWORD=123456
```

也可以通过环境变量覆盖：

```bash
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=your_password
```

Windows PowerShell：

```powershell
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="your_password"
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

如果本机没有全局 Maven，可以使用 IDE 自带 Maven 或 Maven Wrapper 缓存中的 Maven。

### 4. 打开系统

Web UI：

```text
http://localhost:8080/
```

接口文档：

```text
http://localhost:8080/doc.html
```

健康检查：

```text
http://localhost:8080/api/health
```

## 常用接口

- `POST /api/transaction/add` 添加交易
- `DELETE /api/transaction/{id}` 删除交易
- `GET /api/transaction/{id}` 查询交易详情
- `GET /api/transaction/list?month=2026-06&category=餐饮` 查询交易列表
- `GET /api/transaction/report?month=2026-06` 查询月度报表
- `POST /api/budget/save` 新增预算
- `PUT /api/budget/update` 更新预算
- `DELETE /api/budget/{id}` 删除预算
- `GET /api/budget/list?month=2026-06` 查询预算列表
- `GET /api/budget/check?month=2026-06` 检查超支

## 项目结构

```text
src/main/java/com/example/finance
├── common
├── config
├── controller
├── dto
├── entity
├── enums
├── mapper
├── service
└── vo

src/main/resources/static
├── index.html
├── styles.css
└── app.js
```

## 开源协议

MIT License
