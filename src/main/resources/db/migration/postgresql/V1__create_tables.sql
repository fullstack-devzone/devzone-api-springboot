create sequence user_id_seq start with 1 increment by 50;
create sequence post_id_seq start with 1 increment by 50;

create table users (
    id bigint DEFAULT nextval('user_id_seq') not null,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(20) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT user_email_unique UNIQUE(email)
);

create table posts (
    id bigint DEFAULT nextval('post_id_seq') not null,
    url varchar(1024) not null,
    title varchar(1024) not null,
    content text,
    created_by bigint not null REFERENCES users(id),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
