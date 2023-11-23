package gametracker.backend.modules

import gametracker.backend.http.routes.*
import gametracker.backend.repository.*

import cats.effect.IO
import cats.syntax.all.*
import doobie.Transactor
import org.http4s.*
import org.http4s.headers.Origin
import org.http4s.implicits.*
import org.http4s.server.middleware.{CORS, ErrorAction, RequestLogger}
import org.typelevel.log4cats.LoggerFactory

final class HttpApi(xa: Transactor[IO])(using lf: LoggerFactory[IO]) {

   val logger = LoggerFactory[IO].getLogger

   private val gameRep   = new GameRepo(xa)
   private val playerRep = new PlayerRepo(xa)
   private val matchRep  = new MatchRepo(xa)

   private val gameRoutes   = new GameRoutes(gameRep)
   private val playerRoutes = new PlayerRoutes(playerRep)
   private val matchRoutes  = new MatchRoutes(matchRep)

   private val allRoutes = (gameRoutes.routes <+> playerRoutes.routes <+> matchRoutes.routes)

   private val middleware: HttpRoutes[IO] => HttpRoutes[IO] = { (http: HttpRoutes[IO]) =>
      RequestLogger.httpRoutes[IO](
        logHeaders = true,
        logBody = true,
        redactHeadersWhen = _ => false,
        logAction = Some((msg: String) => IO.println(msg))
      )(http)
   } andThen { (http: HttpRoutes[IO]) =>
      CORS.policy.withAllowOriginAll.withAllowMethodsAll.withAllowHeadersAll
         .withAllowCredentials(false)
         .apply(http)
   } andThen { (http: HttpRoutes[IO]) =>
      ErrorAction.httpRoutes[IO](
        http,
        (req, thr) => IO.println(thr.getMessage())
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
