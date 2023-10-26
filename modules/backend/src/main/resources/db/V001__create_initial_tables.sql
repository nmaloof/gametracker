create table game (
    id integer primary key,
    name text not null unique
) strict;

create table player (
    id integer primary key,
    username text not null unique
) strict;

create table match (
    id integer primary key,
    game_id integer,
    foreign key (game_id) references game(id)
) strict;

create table match_detail (
    match_id integer,
    player_id integer,
    team_id integer,
    score integer,
    foreign key (match_id) references match(id),
    foreign key (player_id) references player(id)
) strict;