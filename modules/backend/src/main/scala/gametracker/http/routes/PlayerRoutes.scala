package gametracker.backend.http.routes

import gametracker.backend.algebras.PlayerAlg
import gametracker.backend.domain.{Account, PlayerAlreadyExists}
import gametracker.backend.http.Codecs.given
import gametracker.shared.domain.*

import cats.effect.IO
import cats.implicits.*
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}

class PlayerRoutes(player: PlayerAlg, middleware: AuthMiddleware[IO, Account]) extends Http4sDsl[IO] {
   private val prefixPath = "/players"

   private val authRoutes = AuthedRoutes.of[Account, IO] { 
      case req @ POST -> Root / "create" as account => {
         val reply = for {
            ply <- req.req.as[PlayerParam]
            res <- player.insert(ply)
            resp <- Ok()
         } yield resp

         reply.handleErrorWith {
            case PlayerAlreadyExists(name) => Conflict("Player name already exists")
         }
      }

   }

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root / "all" => Ok(player.findAll())

      case GET -> Root / LongVar(id) => player.findById(id).foldF(NotFound())(Ok(_))

      case GET -> Root / LongVar(id) / "games" => Ok(player.findGamesPlayed(id))

      case GET -> Root / "name" / name => player.findByName(name).foldF(NotFound())(Ok(_))
   }

   val routes = Router(prefixPath -> (httpRoutes <+> middleware(authRoutes)))
}
