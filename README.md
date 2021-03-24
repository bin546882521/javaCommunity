## springboot 测试案例

## 资料   资料
[Spring 文档](https://spring.io/guides)
[http请求](https://square.github.io/okhttp/)

##脚本
```sql
create table JAVAUSER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    constraint JAVAUSER_PK
        primary key (ID)
);
```