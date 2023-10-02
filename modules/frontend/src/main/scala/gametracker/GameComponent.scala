package gametracker

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import gametracker.SearchBox.Game

object GameComponent {
  def render(searchResults: Signal[List[Game]]): Element = {
    def gameRender(game: Game): Element = div(
        p(s"ID: ${game.id}"),
        p(s"Name: ${game.name}")
    )


    div(
        children <-- searchResults.map(lg => lg.map(gameRender))
    )
  }

}
