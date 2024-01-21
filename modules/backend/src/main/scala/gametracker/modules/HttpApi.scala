package gametracker.backend.modules

import gametracker.backend.algebras.*
import gametracker.backend.config.AppConfig
import gametracker.backend.http.routes.*
import gametracker.backend.repository.*

import cats.effect.IO
import cats.syntax.all.*
import dev.profunktor.auth.JwtAuthMiddleware
import dev.profunktor.auth.jwt.{JwtAuth, JwtToken}
import dev.profunktor.redis4cats.RedisCommands
import doobie.Transactor
import io.circe.parser.decode
import org.http4s.*
import org.http4s.headers.Origin
import org.http4s.implicits.*
import org.http4s.server.middleware.{CORS, ErrorAction, RequestLogger}
import org.typelevel.log4cats.LoggerFactory
import pdi.jwt.{JwtAlgorithm, JwtClaim}

final class HttpApi(xa: Transactor[IO], config: AppConfig, redis: RedisCommands[IO, String, String])(using lf: LoggerFactory[IO]) {

   val logger = LoggerFactory[IO].getLogger

   private val algebras = Algebras(xa)

   // --- Auth Stuff
   val security           = Security(config, redis)
   private val auth       = new AuthRepo(security.token, algebras.accounts, redis)
   private val authRoutes = new AuthRoutes(auth, security.jwtMiddleware)
   // --- End Auth Stuff

   private val gameRoutes   = new GameRoutes(algebras.games, security.jwtMiddleware)
   private val playerRoutes = new PlayerRoutes(algebras.players, security.jwtMiddleware)
   private val matchRoutes  = new MatchRoutes(algebras.matches)

   private val allRoutes = (gameRoutes.routes <+> playerRoutes.routes <+> matchRoutes.routes <+> authRoutes.routes)

   private val middleware: HttpRoutes[IO] => HttpRoutes[IO] = { (http: HttpRoutes[IO]) =>
      RequestLogger.httpRoutes[IO](
        logHeaders = true,
        logBody = true,
        redactHeadersWhen = _ => false,
        logAction = Some((msg: String) => logger.debug(msg))
      )(http)
   } andThen { (http: HttpRoutes[IO]) =>
      CORS.policy.withAllowOriginAll.withAllowMethodsAll.withAllowHeadersAll
         .withAllowCredentials(false)
         .apply(http)
   } andThen { (http: HttpRoutes[IO]) =>
      ErrorAction.httpRoutes[IO](
        http,
        (req, thr) => logger.error(req.toString + thr.getMessage())
      )
   }

   // withAllowOriginHost(Set(
   //       Origin.Host(Uri.Scheme.https, Uri.RegName("localhost"), Some(8080)),
   //       Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(8080))
   //    ))
   // private val corsService = CORS.policy.withAllowOriginAll.apply(errorReq.orNotFound)
   // private val corsService = CORS.policy.withAllowOriginHost(
   //      Set(
   //        Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(8080))
   //      )
   //    ).apply(errorReq.orNotFound)

   val httpApp = middleware(allRoutes).orNotFound

}
