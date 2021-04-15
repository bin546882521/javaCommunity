## springboot 测试案例

## 资料   资料
[Spring 文档](https://spring.io/guides)
[http请求](https://square.github.io/okhttp/)
[flyway](https://flywaydb.org/documentation/getstarted/firststeps/gradle)
[thymeleaf](https://www.thymeleaf.org)
[springMvc](https://docs.spring.io/spring-framework/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)
[postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)
##脚本
```sql
create table JAVAUSER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT
);
```
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```