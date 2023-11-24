package gametracker.frontend

import com.raquo.laminar.api.L.{*, given}
import io.laminext.fetch.{Fetch, FetchResponse}
import io.laminext.fetch.circe.*
import org.scalajs.dom

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

import concurrent.ExecutionContext.Implicits.global
import gametracker.shared.domain.*

object SearchBox {

  given playerDecoder: Decoder[Player]           = deriveDecoder[Player]
  given gameDecoder: Decoder[Game]           = deriveDecoder[Game]
  given matchViewDecoder: Decoder[MatchView] = deriveDecoder[MatchView]
  
  val searchStringVar = Var("")

   def render(formSubmitted: Observer[Boolean], responseReceived: FetchResponse[List[MatchView]] => Unit): Element = {
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
            Fetch.get(s"http://localhost:8080/player/name/$searchString").decode[Player].flatMap { player => 
              formSubmitted.onNext(true)
              Fetch.get(s"http://localhost:8080/match/search?playerId=${player.data.id}").decode[List[MatchView]].map { mv =>
                mv
              }
            }
          } --> responseReceived
        )
      )
   }
}
