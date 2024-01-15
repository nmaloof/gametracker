package gametracker.backend.http.routes

import gametracker.backend.algebras.GameAlg
import gametracker.backend.domain.{Account, GameAlreadyExists}
import gametracker.backend.http.Codecs.given
import gametracker.shared.domain.*

import cats.effect.IO
import cats.implicits.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}

class GameRoutes(game: GameAlg, middleware: AuthMiddleware[IO, Account]) extends Http4sDsl[IO] {

   private val prefixPath = "/games"

   private val authRoutes = AuthedRoutes.of[Account, IO] {
      case req @ POST -> Root / "create" as account => {
         val reply = for {
            gme  <- req.req.as[GameParam]
            res  <- game.insert(gme)
            resp <- Ok()
         } yield resp

         reply.handleErrorWith { case GameAlreadyExists(game) =>
            Conflict("Game name already exists")
         }
      }

   }

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root / "all" => Ok(game.findAll())

      case GET -> Root / LongVar(id) => game.findById(id).foldF(NotFound())(Ok(_))
   }

   val routes = Router(prefixPath -> (httpRoutes <+> middleware(authRoutes)))
}
