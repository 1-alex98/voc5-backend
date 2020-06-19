-- Account
create table if not exists account
(
    id       serial primary key,
    email    varchar(255) not null unique,
    password varchar(255) not null
);

-- Vocabulary
create table if not exists vocabulary
(
    id       serial primary key,
    owner    int references account (id) not null,
    question text                        not null,
    answer   text                        not null,
    language varchar(255)                not null,
    phase    smallint                    not null default 0
);
