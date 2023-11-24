package gametracker.frontend

import gametracker.shared.domain.*
import gametracker.frontend.components.MatchViewComponent

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import io.laminext.fetch.{Fetch, FetchResponse}
import scala.util.Success

object Main {

   def main(args: Array[String]): Unit = {
      windowEvents(_.onLoad).foreach { _ =>
         lazy val appContainer = dom.document.querySelector("#appContainer")
         val appElement        = div(h1("Hello world, my friend!"))

         val formSubmitted                       = Var[Boolean](false)
         val searchResults                       = Var[List[MatchView]](List.empty)
         val (responsesStream, responseReceived) = EventStream.withCallback[FetchResponse[List[MatchView]]]

         val something = responsesStream.recoverToTry.collect { case Success(response) => response.data }

         val root = div(
           SearchBox.render(formSubmitted.writer, responseReceived),
           child <-- formSubmitted.signal.map(a => if !a then emptyNode else MatchViewComponent.render(something.toSignal(List.empty)))
         )

         render(appContainer, root)
      }(unsafeWindowOwner)
   }
}
