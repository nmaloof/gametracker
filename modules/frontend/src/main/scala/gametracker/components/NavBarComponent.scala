package gametracker.frontend.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object NavBarComponent {
   def render(): Element = {
      navTag(
        cls := "navBar",
        ul(
          li(
            "Log In"
          )
        )
      )
   }

}
