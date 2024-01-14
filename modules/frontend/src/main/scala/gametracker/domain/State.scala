package gametracker.domain

final case class State(
    loggedIn: LoggedIn
)

final case class LoggedIn(
    isLoggedIn: Boolean,
    token: Option[String]
)
