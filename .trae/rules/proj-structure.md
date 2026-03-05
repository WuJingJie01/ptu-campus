后端开发规范
1. 分层约束：严格遵循 Controller → Service → Mapper 分层，禁止跨层调用
2. 依赖注入：必须使用 @RequiredArgsConstructor，禁止使用 @Autowired
3. 事务规范：事务注解必须指定 rollbackFor = Exception.class
4. 接口规范：
  - 统一使用 RESTful 风格
  - 统一响应体：Result<T>
  - HTTP 方法语义：GET 查询、POST 创建、PUT 全量更新、PATCH 局部更新、DELETE 删除
5. 异常处理：
  - 统一使用 GlobalExceptionHandler 捕获异常
  - 异常信息必须在 com.campus.common.error.CommonErrorCode 中定义
  - 禁止直接抛出 RuntimeException，必须使用自定义异常
6. 数据库规范：
  - 表必须包含 id、create_time、update_time、is_deleted 字段
  - 禁止使用 SELECT *
  - 禁止循环操作数据库，优先批量操作
  - 合理创建索引，避免过度索引
7. 命名规范：
  - 包名：全小写，点分隔（如 com.campus.forum.controller）
  - 类名：大驼峰，名词开头（如 ForumPostController）
  - 方法名：小驼峰，动词开头（如 getPostList）
  - 常量名：全大写，下划线分隔（如 MAX_PAGE_SIZE）
  - 数据库表 / 字段：小写，下划线分隔（如 forum_post、post_title）