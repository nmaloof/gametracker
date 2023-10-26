package gametracker.domain

final case class Player(
    id: Int,
    username: String
)

case class PlayerParam(username: String)
