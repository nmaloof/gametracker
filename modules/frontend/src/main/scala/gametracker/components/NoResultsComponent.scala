package gametracker.frontend.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object NoResultsComponent {
   def render(): Element = div(
     h1("No Element Found")
   )
}
