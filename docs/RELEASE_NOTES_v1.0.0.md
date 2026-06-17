# v1.0.0 Release Notes

第一个完整可运行版本，适合课程设计、练手项目和二次开发。

## 新功能

- 登录注册：支持用户注册和登录，密码使用 BCrypt 加密存储。
- 交易流水：支持添加、删除、查询收入和支出记录。
- 自定义分类：交易和预算分类都支持自由输入。
- 预算管理：支持按月份、分类设置预算。
- 超支检查：统计分类支出并返回超出预算的分类。
- 月度报表：统计总收入、总支出、结余、分类支出和每日趋势。
- Web UI：内置登录页、仪表盘、流水页、预算页、报表页。
- 接口文档：集成 Knife4j / OpenAPI。

## 技术栈

- Java 17+
- Spring Boot 3.1.5
- MyBatis-Plus 3.5.3.1
- MySQL 8+
- Hutool
- Lombok
- Knife4j

## 快速启动

```bash
git clone https://github.com/jeremylin717/finance-system.git
cd finance-system
mysql -uroot -p --default-character-set=utf8mb4 < sql/init.sql
mvn spring-boot:run
```

打开：

```text
http://localhost:8080/
```

接口文档：

```text
http://localhost:8080/doc.html
```

## 后续计划

- JWT 登录认证
- 分页查询流水
- Excel 导入导出
- Docker Compose 一键启动
- 更完整的图表可视化
- Vue / React 前端版本
