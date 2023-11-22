package gametracker.shared.domain

final case class Player(
    id: Int,
    username: String
)

case class PlayerParam(username: String)
