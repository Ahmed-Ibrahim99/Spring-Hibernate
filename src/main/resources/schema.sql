DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS
(
    user_id   bigint      not null,
    user_name varchar(25) not null,
    email     varchar(50) not null,
    password  varchar(50) not null
);