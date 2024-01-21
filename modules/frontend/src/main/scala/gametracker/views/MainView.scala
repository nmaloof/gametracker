package gametracker.frontend.views

import gametracker.shared.domain.*
import gametracker.frontend.SearchBox
import gametracker.frontend.components.*

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import io.laminext.fetch.{Fetch, FetchResponse}
import scala.util.Success

object MainView {

   val formSubmitted                       = Var[Boolean](false)
   val (matchResponseStream, matchResponseRecieved) = EventStream.withCallback[FetchResponse[List[MatchView]]]
   val something = matchResponseStream.recoverToTry.collect { case Success(response) => response.data }
   
   
   def render(): Element = {
      div(
         "This is the Main Page (from router)",
         SearchBox.render(formSubmitted.writer, matchResponseRecieved),
         child <-- formSubmitted.signal.map(a => if !a then emptyNode else MatchViewComponent.render(something.toSignal(List.empty)))
      )
   }
}
