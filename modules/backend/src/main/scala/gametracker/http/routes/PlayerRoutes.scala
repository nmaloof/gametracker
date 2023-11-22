package gametracker.backend.http.routes

import gametracker.shared.domain.*
import gametracker.backend.algebras.PlayerAlg
import gametracker.backend.http.Codecs.given

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class PlayerRoutes(player: PlayerAlg) extends Http4sDsl[IO] {
   private val prefixPath = "/player"

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root => Ok("player route")

      case GET -> Root / LongVar(id) => player.findById(id).foldF(NotFound())(Ok(_))

      case GET -> Root / LongVar(id) / "games" => Ok(player.findGamesPlayed(id))

      case GET -> Root / "name" / name => player.findByName(name).foldF(NotFound())(Ok(_))

      case req @ POST -> Root / "create" =>
         for {
            ply  <- req.as[PlayerParam]
            res  <- player.insert(ply)
            resp <- res.fold(e => BadRequest(e.getMessage()), v => Ok())
         } yield resp
   }

   val routes = Router(prefixPath -> httpRoutes)
}
