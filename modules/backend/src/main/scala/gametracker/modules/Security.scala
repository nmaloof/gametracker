package gametracker.backend.modules

import gametracker.backend.algebras.Token
import gametracker.backend.config.AppConfig
import gametracker.backend.domain.Account

import cats.effect.IO
import dev.profunktor.auth.JwtAuthMiddleware
import dev.profunktor.auth.jwt.{JwtAuth, JwtToken}
import org.typelevel.log4cats.LoggerFactory
import pdi.jwt.{JwtAlgorithm, JwtClaim}

final class Security(config: AppConfig, inMemDb: scala.collection.mutable.Map[String, String])(using lf: LoggerFactory[IO]) {
   private def authenticate(token: JwtToken)(claim: JwtClaim): IO[Option[Account]] = IO {
      inMemDb.get(token.value).map(_ => Account("", ""))
   }

   val jwtMiddleware = JwtAuthMiddleware[IO, Account](
     JwtAuth.hmac(config.securityConfig.secretKey, JwtAlgorithm.HS256),
     authenticate
   )

   val token = new Token(config.securityConfig.secretKey)

}
