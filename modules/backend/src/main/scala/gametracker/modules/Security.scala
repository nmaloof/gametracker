package gametracker.backend.modules

import gametracker.backend.config.AppConfig
import gametracker.backend.domain.Account
import gametracker.backend.services.Token

import cats.effect.IO
import dev.profunktor.auth.JwtAuthMiddleware
import dev.profunktor.auth.jwt.{JwtAuth, JwtToken}
import dev.profunktor.redis4cats.RedisCommands
import org.typelevel.log4cats.LoggerFactory
import pdi.jwt.{JwtAlgorithm, JwtClaim}

final class Security(config: AppConfig, redis: RedisCommands[IO, String, String])(using lf: LoggerFactory[IO]) {

   private def authenticate(token: JwtToken)(claim: JwtClaim): IO[Option[Account]] = redis.get(token.value).map(_.map(_ => Account("", "")))

   val jwtMiddleware = JwtAuthMiddleware[IO, Account](
     JwtAuth.hmac(config.securityConfig.secretKey, JwtAlgorithm.HS256),
     authenticate
   )

   val token = new Token(config.securityConfig.secretKey)

}
