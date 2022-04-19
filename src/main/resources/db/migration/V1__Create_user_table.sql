create table user
(
    id int auto_increment primary key not null,
    account_id varchar(100),
    name varchar(50),
    token varchar(64),
    gmt_create bigint,
    gmt_modified bigint,
    password varchar(50),
    email varchar(256)
);