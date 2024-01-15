create table account (
    id text primary key,
    username text not null unique,
    password text not null
);

create table player (
    id serial primary key,
    name text not null unique
);

create table game (
    id serial primary key,
    name text not null unique
);

create table match (
    id serial primary key,
    game_id integer,
    foreign key (game_id) references game(id)
);

create table match_detail (
    match_id integer,
    player_id integer,
    team_id integer,
    score integer,
    foreign key (match_id) references match(id),
    foreign key (player_id) references player(id)
);
