RobotConversation

RobotConversation 是一个基于 Spring Boot 和 LangChain4j 构建的机器人对话应用，集成了阿里云 DashScope 大模型服务，支持与机器人进行智能对话交互。

项目特点

基于 Spring Boot 3.5.7 构建，提供稳定的后端服务架构

集成 LangChain4j 框架，简化大模型交互逻辑

使用阿里云 DashScope 服务（默认配置 qwen-max 模型）

包含 Redis 集成，可用于会话状态管理

支持 MySQL 数据库集成，方便存储对话历史等数据

引入 Hutool 工具库，提供丰富的工具类支持

使用 Lombok 简化代码开发

技术栈

核心框架：Spring Boot 3.5.7

大模型集成：LangChain4j 1.0.0-beta3、DashScope SDK 2.21.0

数据存储：Redis、MySQL

工具库：Hutool 5.8.38、Lombok 1.18.38

构建工具：Maven 3.9.11

环境要求

JDK 17 及以上

Maven 3.6+（或使用项目内置的 maven-wrapper）

Redis 服务器（默认配置：localhost:6379）

MySQL 数据库（可选，根据实际业务需求配置）

阿里云 DashScope API 密钥（需自行申请）
