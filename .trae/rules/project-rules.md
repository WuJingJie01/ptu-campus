项目为微服务架构
后端使用 
SpringBoot 3.5.0
SpringCloud 2025.0.0.0
SpringCloudAlibaba 2025.0.0.0
路由使用 Gateway
注册中心使用 Nacos 3.1.0
限流使用 Sentinel 1.8.8
缓存使用 Redis 5.0.14
前端使用 Vue3 + ElementPlus

系统采用前后端分离架构
采用网关统一入口
服务间使用 OpenFeign 调用
统一返回结构 Result<T>
统一异常处理
统一日志规范
接口必须带接口说明
所有模块必须遵循领域划分

Controller 只负责接收请求
Service 负责业务逻辑
Repository 负责数据访问
DTO 和 Entity 分离
所有接口必须使用 RESTful 风格

campus-common 
   - Result<T> 统一返回类
   - 全局异常类
   - 常量类
   - JWT 工具类（后面放）
   - 不要放业务逻辑。
