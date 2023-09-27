package gametracker

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object Main {

   // def main(args: Array[String]): Unit = {
   //    println("Hello World!")
   //    lazy val appContainer = dom.document.querySelector("#appContainer")
   //    val appElement = div(h1("Hello world"))
   //    renderOnDomContentLoaded(appContainer, appElement)
   // }
   def main(args: Array[String]): Unit = {
      windowEvents(_.onLoad).foreach { _ =>
         lazy val appContainer = dom.document.querySelector("#appContainer")
         val appElement        = div(h1("Hello world, my friend!"))

         val sb = SearchBox.create()
         val root = div(
           appElement,
           sb.node,
           child <-- sb.signal.flatMap { s =>
              Signal.fromValue(h1(s))
           },
           div(
             children <-- Sample.gameElementsSignal
           ),
           div(
             child <-- Sample2.xElementsStream
           )
         )
         render(appContainer, root)
      }(unsafeWindowOwner)
   }
}
