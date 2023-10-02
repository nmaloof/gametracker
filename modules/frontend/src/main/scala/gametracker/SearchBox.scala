package gametracker

import com.raquo.laminar.api.L.{*, given}
import io.laminext.fetch.{Fetch, FetchResponse}
import io.laminext.fetch.circe.*
import org.scalajs.dom

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

import concurrent.ExecutionContext.Implicits.global


object SearchBox {

   val searchStringVar = Var("")
   

   case class Game(id: Int, name: String)
   given gameDecoder: Decoder[Game] = deriveDecoder[Game]
   
   def render(formSubmitted: Observer[Boolean], responseReceived: FetchResponse[List[Game]] => Unit): Element = {
      div(
         cls := "flex justify-center",
         input(
            placeholder := "Player Name",
            controlled(
               value <-- searchStringVar.signal,
               onInput.mapToValue --> searchStringVar.writer
            )
         ),
         button(
            "Search",
            onClick.mapTo(searchStringVar.now()).flatMap { case (searchString) =>
               Fetch.get("http://localhost:8080/game/all").decode[List[Game]].map {r =>
                  formSubmitted.onNext(true)
                  r
               }
            } --> responseReceived
         )
      )
   }
}
