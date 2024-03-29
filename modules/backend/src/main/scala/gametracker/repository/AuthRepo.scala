package gametracker.backend.repository

import gametracker.backend.algebras.*

import cats.effect.IO
import cats.implicits.*
import dev.profunktor.auth.jwt.JwtToken
import dev.profunktor.redis4cats.RedisCommands
import org.typelevel.log4cats.LoggerFactory

import concurrent.duration.DurationInt

class AuthRepo(token: TokenAlg, account: AccountAlg, redis: RedisCommands[IO, String, String])(using lf: LoggerFactory[IO])
    extends AuthAlg {

   case class InvalidUsernameOrPassword(username: String) extends scala.util.control.NoStackTrace

   private val logger = LoggerFactory[IO].getLogger

   override def login(username: String, password: String): IO[JwtToken] = {
      account
         .findAccount(username, password)
         .semiflatMap { act =>
            redis.get(act.username).flatMap {
               case None =>
                  token.create.flatTap { t =>
                     redis.setEx(t.value, act.toString(), 60.seconds)
                  }
               case Some(value) => IO.pure(JwtToken(value))
            }
         }
         .getOrElseF(InvalidUsernameOrPassword(username).raiseError[IO, JwtToken])
   }

   override def logout(token: JwtToken): IO[Unit] = {
      for {
         _       <- logger.info("Logged out. Deleting key from redis")
         keysVal <- redis.del(token.value)
      } yield ()
   }
}
