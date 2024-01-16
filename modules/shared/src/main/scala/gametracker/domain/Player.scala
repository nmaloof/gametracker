package gametracker.shared.domain

final case class Player(
    id: Int,
    name: String
)

case class PlayerParam(name: String)
