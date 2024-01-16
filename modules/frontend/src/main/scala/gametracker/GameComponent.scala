package gametracker

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import gametracker.shared.domain.Game

object GameComponent {
   def render(searchResults: Signal[List[Game]]): Element = {
      def gameRender(game: Game): Element = li(
        div(
          p(s"ID: ${game.id}"),
          p(s"Name: ${game.name}")
        )
      )

      div(
        cls := "container",
        ul(
          children <-- searchResults.map(lg => lg.map(gameRender))
        )
      )
   }

}
