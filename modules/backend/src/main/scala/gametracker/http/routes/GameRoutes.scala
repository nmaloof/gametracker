package gametracker.http.routes

import gametracker.algebras.GameAlg
import gametracker.domain.Game
import gametracker.http.Codecs.given

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class GameRoutes(game: GameAlg) extends Http4sDsl[IO] {

   private val prefixPath = "/game"

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root / "all" => Ok(game.findAll())

      case GET -> Root / LongVar(id) => game.findById(id).foldF(NotFound())(Ok(_))

      case req @ POST -> Root / "create" =>
         for {
            gme    <- req.as[Game]
            result <- game.insert(gme)
            resp   <- result.fold(e => BadRequest(e.getMessage()), v => Ok())
         } yield resp
   }

   val routes = Router(prefixPath -> httpRoutes)
}
