package gametracker.frontend

import gametracker.shared.domain.*
import gametracker.frontend.components.*
import gametracker.frontend.views.*

import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import org.scalajs.dom
import io.laminext.fetch.{Fetch, FetchResponse}
import scala.util.Success

object Main {

   def renderPage(page: Page) = {
      page match
         case MainPage  => div("This is the Main Pages")
         case LoginPage => div(LoginView.render())

   }

   // def main(args: Array[String]): Unit = {
   //    windowEvents(_.onLoad).foreach { _ =>
   //       lazy val appContainer    = dom.document.querySelector("#appContainer")
   //       lazy val navBarContainer = dom.document.querySelector("#navBarContainer")

   //       val root = div(
   //         NavBarComponent.render()
   //       )
   //       val appRoot = div(
   //         h1("Routing Example"),
   //         child <-- router.currentPageSignal.map(x => {
   //            println(x)
   //            renderPage(x)
   //         })
   //       )

   //       render(appContainer, appRoot)
   //       render(navBarContainer, root)
   //    }(unsafeWindowOwner)
   // }
   // def main(args: Array[String]): Unit = {
   //    windowEvents(_.onLoad).foreach { _ =>
   //       lazy val appContainer = dom.document.querySelector("#appContainer")
   //       lazy val navBarContainer = dom.document.querySelector("#navBarContainer")

   //       val navRoot = div(
   //          NavBarComponent.render()
   //       )

   //       val appRoot = div(
   //          LoginForm.render()
   //       )

   //       render(navBarContainer, navRoot)
   //       render(appContainer, appRoot)
   //    }(unsafeWindowOwner)
   // }

   def main(args: Array[String]): Unit = {
      windowEvents(_.onLoad).foreach { _ =>
         lazy val appContainer = dom.document.querySelector("#appContainer")
         val appElement        = div(h1("Hello world, my friend!"))

         val formSubmitted                       = Var[Boolean](false)
         val searchResults                       = Var[List[MatchView]](List.empty)
         val (responsesStream, responseReceived) = EventStream.withCallback[FetchResponse[List[MatchView]]]

         val something = responsesStream.recoverToTry.collect { case Success(response) => response.data }

         val root = div(
           appElement,
           SearchBox.render(formSubmitted.writer, responseReceived),
           child <-- formSubmitted.signal.map(a => if !a then emptyNode else MatchViewComponent.render(something.toSignal(List.empty)))
         )

         render(appContainer, root)
      }(unsafeWindowOwner)
   }
}
