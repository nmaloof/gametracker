package gametracker.frontend

import com.raquo.waypoint.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import io.circe.{Encoder, Decoder}
import io.circe.generic.auto.*
import cats.syntax.functor.*
import io.circe.syntax.*, io.circe.parser.decode

sealed trait Page
case object MainPage  extends Page
case object LoginPage extends Page

object Page {
   given Encoder[Page] = Encoder.instance {
      case mp @ MainPage  => "MainPage".asJson
      case lp @ LoginPage => "LoginPage".asJson
   }

   // given Decoder[Page] = List[Decoder[Page]](
   //     Decoder[MainPage].widen,
   //     Decoder[LoginPage].widen
   // ).reduceLeft(_ or _)
}

val router = new Router[Page](
  routes = List(
    Route.static(MainPage, root / endOfSegments),
    Route.static(LoginPage, root / "login")
  ),
  serializePage = page => summon[Encoder[Page]](page).noSpaces,
  deserializePage = str => (decode[Page](str)).getOrElse(MainPage),
  getPageTitle = {
     case MainPage  => "GameTracker - Home"
     case LoginPage => "GameTracker - Login"
  },
  routeFallback = _ => LoginPage
)(
  popStateEvents = L.windowEvents(_.onPopState),
  owner = L.unsafeWindowOwner
)
