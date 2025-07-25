create table users
(
    account_non_expired     bit                                     not null,
    account_non_locked      bit                                     not null,
    credentials_non_expired bit                                     not null,
    enabled                 bit                                     not null,
    created_at              datetime(6)                             null,
    last_login_at           datetime(6)                             null,
    id                      binary(16)                              not null
        primary key,
    email                   varchar(255)                            not null,
    first_name              varchar(255)                            not null,
    last_name               varchar(255)                            not null,
    password                varchar(255)                            not null,
    role                    enum ('ADMIN', 'INSTRUCTOR', 'STUDENT') not null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email)
);

create table instructors
(
    id         binary(16)   not null
        primary key,
    department varchar(255) not null,
    email      varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) null,
    user_id    binary(16)   null,
    username   varchar(36)  not null,
    constraint UK1p61qho6k9oewkyd5uv1aniv7
        unique (email),
    constraint UK9bnko4773vvkd5f2pdbxvsly3
        unique (user_id),
    constraint FKds2m3jgxj98sd5mr1qw23ecjp
        foreign key (user_id) references users (id)
);
