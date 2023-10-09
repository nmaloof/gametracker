package gametracker.http.routes

import gametracker.algebras.MatchAlg
import gametracker.http.Codecs.given

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.server.Router

class MatchRoutes(mtch: MatchAlg) extends Http4sDsl[IO] {
   private val prefixPath = "/match"

   object GameParam   extends OptionalQueryParamDecoderMatcher[Long]("gameId")
   object PlayerParam extends OptionalQueryParamDecoderMatcher[Long]("playerId")

   private val httpRoutes = HttpRoutes.of[IO] {
      case GET -> Root / "all" => Ok(mtch.findAll())

      case GET -> Root / LongVar(id) => mtch.findById(id).foldF(NotFound())(Ok(_))

      case GET -> Root / "search" :? GameParam(gameId) +& PlayerParam(playerId) =>
         (gameId, playerId) match {
            case (None, None) => BadRequest("Please specify a search parameter")
            case _            => mtch.findBy(playerId, gameId).foldF(NotFound())(Ok(_))
         }

   }

   val routes = Router(prefixPath -> httpRoutes)
}
