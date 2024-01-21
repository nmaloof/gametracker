package gametracker.frontend.domain

import com.raquo.laminar.api.L.{*, given}

final case class AppState private (
    authToken: Var[Option[String]]
)

object AppState {
   def init(): AppState = AppState(
     Var(None)
   )
}
