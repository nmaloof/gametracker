package gametracker.domain

case class Match(
    id: Int,
    game: Game,
    player: Player,
    teamId: Int,
    score: Int
)
