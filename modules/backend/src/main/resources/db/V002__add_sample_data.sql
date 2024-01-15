insert into account (id, username, password) values 
('18703c67-8336-47b5-b1ed-dae8c0963cb3', 'cooluser1', '$argon2id$v=19$m=60000,t=2,p=1$vJnlWgFlhLjXTjpQulZEBA$gZL6v4N5zPRxeahBKEq8/kOHLE/Xqd9gCbpQRAPdNNwEbbMqNOYU+a9b9Re8PjLLCMy8U56g1rnYUkotcWxzWQ'),
('4556a8a6-3662-4dde-a48c-945935609572', 'cooluser2', '$argon2id$v=19$m=60000,t=2,p=1$f4xzyJ3F2NKMUKrlkpRj8w$LGmg1xGPdBTaZKix7PtrrRNnV8cw4W66wom0GEkmrPXrwf6c5ZvM/iV0quGFH0birbpp/konRy410r78G9ZoKw'),
('688d4c27-a218-41cb-ad0e-d1a39ba70132', 'cooluser3', '$argon2id$v=19$m=60000,t=2,p=1$FTz/r2B27jImJKTaIfH9Sg$20za0sTbHbDrvREP3b9nfaloZDZwAZIEO4WNUCdMOtst7VcQyWXp3/MiAuNE1C63gxs24A9RABGjOrxABsz1uw')
;

insert into game (id, name) values 
    (1, 'ping pong'),
    (2, 'billiards'),
    (3, 'basketball')
;

insert into player (id, name) values
    (1, 'Nick'),
    (2, 'Jill'),
    (3, 'David'),
    (4, 'Megan')
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