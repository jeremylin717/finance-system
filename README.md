# Finance System - 个人财务管理系统

一个基于 **Spring Boot 3 + MyBatis-Plus + MySQL** 的个人财务管理系统，内置登录注册、交易流水、预算管理、月度报表和可视化 Web UI。项目适合作为 Java Web 课程设计、毕业设计基础项目、Spring Boot 练手项目或个人记账系统原型。

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.3.1-blue)
![MySQL](https://img.shields.io/badge/MySQL-8+-4479A1)
![License](https://img.shields.io/badge/License-MIT-black)

## 项目亮点

- **完整业务闭环**：登录注册、收入支出、预算、报表、超支检查全部可用。
- **内置前端页面**：无需额外启动 Vue/React，访问 `http://localhost:8080/` 即可使用。
- **统一接口返回**：所有 Controller 返回 `Result<T>`，前后端交互清晰。
- **MyBatis-Plus 简洁开发**：基础 CRUD 使用 `BaseMapper`，统计查询使用注解 SQL。
- **支持自定义分类**：交易和预算分类不限制固定字典，可自由输入。
- **密码安全存储**：注册密码使用 BCrypt 加密，不以明文存入数据库。
- **接口文档完善**：集成 Knife4j / OpenAPI，便于调试和二次开发。

## 页面功能

系统首页包含一套完整的财务工作台：

- 登录 / 注册页面
- 财务总览仪表盘
- 总收入、总支出、结余统计
- 最近流水列表
- 添加收入 / 支出交易
- 自定义分类
- 按月份、分类筛选流水
- 月度分类支出统计
- 每日支出趋势
- 预算新增、编辑、删除
- 预算超支检查

## 技术栈

| 层级 | 技术 |
| --- | --- |
| 后端框架 | Spring Boot 3.1.5 |
| ORM | MyBatis-Plus 3.5.3.1 |
| 数据库 | MySQL 8+ |
| 接口文档 | Knife4j / OpenAPI 3 |
| 工具库 | Hutool |
| 代码简化 | Lombok |
| 密码加密 | Spring Security Crypto BCrypt |
| 前端 | HTML + CSS + JavaScript |

## 项目结构

```text
finance-system
├── pom.xml
├── sql
│   └── init.sql
├── src/main/java/com/example/finance
│   ├── FinanceApplication.java
│   ├── common
│   │   ├── Result.java
│   │   ├── ResultCode.java
│   │   └── GlobalExceptionHandler.java
│   ├── config
│   │   ├── CorsConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── enums
│   ├── mapper
│   ├── service
│   └── vo
└── src/main/resources
    ├── application.yml
    └── static
        ├── index.html
        ├── styles.css
        └── app.js
```

## 数据库设计

初始化脚本位于：

```text
sql/init.sql
```

包含以下表：

| 表名 | 说明 |
| --- | --- |
| `finance_user` | 用户表，保存登录账号和 BCrypt 密码 |
| `transaction` | 交易流水表，保存收入和支出记录 |
| `budget` | 月度预算表，保存用户每月每类预算 |
| `category_dict` | 分类字典表，保存默认分类 |

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/jeremylin717/finance-system.git
cd finance-system
```

### 2. 创建数据库

确保本机已安装并启动 MySQL，然后执行：

```bash
mysql -uroot -p --default-character-set=utf8mb4 < sql/init.sql
```

默认会创建名为 `finance` 的数据库。

### 3. 配置数据库连接

默认配置在 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: ${MYSQL_URL:jdbc:mysql://localhost:3306/finance?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true}
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:123456}
```

可以使用环境变量覆盖默认值。

Linux / macOS：

```bash
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=your_password
```

Windows PowerShell：

```powershell
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="your_password"
```

### 4. 启动项目

```bash
mvn spring-boot:run
```

如果你的电脑没有全局 Maven，可以使用 IntelliJ IDEA 自带 Maven 运行。

### 5. 打开系统

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

## 默认分类

系统内置默认分类，但不强制限制，用户可以自由输入自定义分类。

支出分类：

- 餐饮
- 购物
- 教育
- 交通
- 娱乐

收入分类：

- 兼职
- 生活费
- 奖学金

## 常用接口

### 登录注册

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |

### 交易流水

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/transaction/add` | 添加交易 |
| DELETE | `/api/transaction/{id}` | 删除交易 |
| GET | `/api/transaction/{id}` | 查询交易详情 |
| GET | `/api/transaction/list?userId=1&month=2026-06&category=餐饮` | 查询月度流水 |
| GET | `/api/transaction/report?userId=1&month=2026-06` | 查询月度报表 |

### 预算管理

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/budget/save` | 新增预算 |
| PUT | `/api/budget/update` | 更新预算 |
| DELETE | `/api/budget/{id}` | 删除预算 |
| GET | `/api/budget/list?userId=1&month=2026-06` | 查询预算列表 |
| GET | `/api/budget/check?userId=1&month=2026-06` | 检查预算超支 |

## 示例请求

注册：

```json
POST /api/auth/register
{
  "username": "demo",
  "password": "123456",
  "nickname": "演示用户"
}
```

添加支出：

```json
POST /api/transaction/add
{
  "userId": 1,
  "amount": 35.50,
  "type": 0,
  "category": "餐饮",
  "description": "午餐",
  "transactionDate": "2026-06-17"
}
```

设置预算：

```json
POST /api/budget/save
{
  "userId": 1,
  "category": "餐饮",
  "month": "2026-06",
  "monthlyLimit": 1200.00
}
```

## 开发说明

- `type = 1` 表示收入。
- `type = 0` 表示支出。
- 月份参数格式固定为 `yyyy-MM`，例如 `2026-06`。
- 日期字段使用 `LocalDate`。
- 金额字段使用 `BigDecimal`。
- Mapper 使用注解 SQL，没有 XML Mapper。
- 当前前端登录状态保存在浏览器 `localStorage`，适合课程项目和本地演示；生产环境建议改为 JWT 或 Session。

## 适合扩展的方向

- JWT 登录认证
- 多用户权限控制
- 分页查询流水
- Excel 导入导出
- 图表库增强可视化
- Docker Compose 一键启动
- Vue / React 前端重构
- 移动端适配优化

## 许可证

本项目使用 [MIT License](LICENSE) 开源。
