package gametracker.backend.algebras

import gametracker.backend.domain.Account

import cats.effect.IO
import cats.effect.implicits.*
import cats.implicits.*
import cats.syntax.all.*
import dev.profunktor.auth.jwt.JwtToken

trait AuthAlg {

   def login(username: String, password: String): IO[JwtToken]

   def logout(token: JwtToken): IO[Unit]
}

class Auth(token: TokenAlg, account: AccountAlg, redis: scala.collection.mutable.Map[String, String]) extends AuthAlg { // Change this to actually redis

   case class InvalidUsernameOrPassword(username: String) extends scala.util.control.NoStackTrace

   override def login(username: String, password: String): IO[JwtToken] = {
      account
         .findAccount(username, password)
         .semiflatMap { act =>
            redis.get(act.username) match {
               case None =>
                  token.create.flatTap { t =>
                     IO.apply {
                        redis.addOne((act.toString(), t.value))
                        redis.addOne((t.value, act.toString()))
                     }
                  }
               case Some(value) => IO.pure(JwtToken(value))
            }
         }
         .getOrElseF(InvalidUsernameOrPassword(username).raiseError[IO, JwtToken])
   }

//    val usersdb = Map("John" -> "password")
//    val inmemdb = scala.collection.mutable.Map[String, String]()

//    override def login(username: String, password: String): IO[JwtToken] = {
//       val correctPassword = IO { usersdb.get(username).map(_ == password) }
//       correctPassword.flatMap {
//          case None                  => InvalidUsernameOrPassword(username).raiseError[IO, JwtToken]
//          case Some(value) if !value => InvalidUsernameOrPassword(username).raiseError[IO, JwtToken]
//          case Some(value) =>
//             inmemdb.get(username) match {
//                case Some(value) => IO.pure(JwtToken(value))
//                case None =>
//                   token.create.flatTap { t =>
//                      IO.println(inmemdb) *>
//                         IO { inmemdb += (username -> t.value) }
//                   }
//             }
//       }
//    }

   override def logout(token: JwtToken): IO[Unit] = ???
}
