package gametracker.backend.http.routes

import gametracker.backend.algebras.GameAlg
import gametracker.backend.domain.Account
import gametracker.backend.http.Codecs.given
import gametracker.shared.domain.*

import cats.effect.IO
import cats.implicits.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.server.AuthMiddleware

class GameRoutes(game: GameAlg, middleware: AuthMiddleware[IO, Account]) extends Http4sDsl[IO] {

   private val prefixPath = "/games"

   private val authRoutes = AuthedRoutes.of[Account, IO] { case req @ POST -> Root / "create" as account =>
      for {
         gme  <- req.req.as[GameParam]
         res  <- game.insert(gme)
         resp <- res.fold(e => BadRequest(e.getMessage()), v => Ok())
      } yield resp
   }

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root / "all" => Ok(game.findAll())

      case GET -> Root / LongVar(id) => game.findById(id).foldF(NotFound())(Ok(_))
   }

   val routes = Router(prefixPath -> (httpRoutes <+> middleware(authRoutes)))
}
