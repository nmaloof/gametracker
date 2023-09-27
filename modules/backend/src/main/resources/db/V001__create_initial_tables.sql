create table game (
    id int primary key,
    name text not null
) strict;

create table player (
    id int primary key,
    username text not null
) strict;

create table match (
    id int primary key,
    game_id int,
    foreign key (game_id) references game(id)
) strict;

create table match_detail (
    match_id int,
    player_id int,
    team_id int,
    score int,
    foreign key (match_id) references match(id),
    foreign key (player_id) references player(id)
) strict;