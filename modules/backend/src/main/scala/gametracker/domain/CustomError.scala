package gametracker.backend.domain

sealed trait CustomError
case class PlayerAlreadyExists(playerName: String) extends scala.util.control.NoStackTrace with CustomError
case class GameAlreadyExists(gameName: String)     extends scala.util.control.NoStackTrace with CustomError
