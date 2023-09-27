insert into game (id, name) values 
    (1, "Ping Pong"),
    (2, "Billiards"),
    (3, "Basketball")
;

insert into player (id, username) values
    (1, "Nick"),
    (2, "Jill"),
    (3, "David"),
    (4, "Megan")
;

insert into match (id, game_id) values
    (1, 1),
    (2, 3),
    (3, 1),
    (4, 2)
;

insert into match_detail (match_id, player_id, team_id, score) values
    (1, 1, 1, 21), -- Ping
    (1, 4, 2, 15),
    (2, 2, 1, 11), -- Basket
    (2, 3, 2, 5),
    (3, 1, 1, 15), -- Ping
    (3, 3, 1, 15),
    (3, 2, 2, 9),
    (3, 4, 2, 9),
    (4, 1, 1, 9), -- Billiards
    (4, 3, 2, 7)
;