package gametracker.backend.http.routes

import gametracker.backend.algebras.AuthAlg
import gametracker.backend.domain.Account
import gametracker.backend.http.Codecs.given

import cats.effect.IO
import cats.implicits.*
import cats.syntax.all.*
import dev.profunktor.auth.AuthHeaders
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}

class AuthRoutes(auth: AuthAlg, middleware: AuthMiddleware[IO, Account]) extends Http4sDsl[IO] {

   private val prefixPath = "/auth"

   private val noAuthRoutes = HttpRoutes.of[IO] { case req @ POST -> Root / "login" =>
      req.decode[Account] { act =>
         auth.login(act.username, act.password).flatMap(x => Ok(x.value).map(_.addCookie(ResponseCookie("token", x.value))))
      }
   }

   private val authRoutes = AuthedRoutes.of[Account, IO] { // TODO: Change this type later
      case GET -> Root / "testing" as account => Ok(account.toString())

      case req @ POST -> Root / "logout" as user => {
         AuthHeaders.getBearerToken(req.req).traverse_(token => auth.logout(token)) *> Ok("Logged Out")
         // Ok("Logged Out").map(_.removeCookie("token"))
      }
   }

   val routes = Router(prefixPath -> (noAuthRoutes <+> middleware(authRoutes)))
}
