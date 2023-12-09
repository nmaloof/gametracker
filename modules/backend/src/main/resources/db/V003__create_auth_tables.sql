create table account (
    id integer primary key,
    username text not null unique,
    password text not null unique
) strict;

insert into account (id, username, password) values
(1, "CoolUser1", "notgoodpass"),
(2, "CoolUser2", "worsepassword")
;