package gametracker.domain

final case class Game(
    id: Int,
    name: String
)

case class GameParam(name: String)