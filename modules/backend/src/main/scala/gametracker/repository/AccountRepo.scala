package gametracker.backend.repository

import gametracker.backend.algebras.{AccountAlg, CryptoAlg}
import gametracker.backend.domain.Account

import cats.data.OptionT
import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor

class AccountRepo(xa: Transactor[IO], crypto: CryptoAlg) extends AccountAlg {
   import AccountSql.*

   case class InvalidUsername(username: String) extends scala.util.control.NoStackTrace

   override def findAccount(username: String, password: String): OptionT[IO, Account] = {
      OptionT {
         select(username).option.transact(xa).map { act =>
            act.filter(x => crypto.matches(password, x.password))
         }
      }
   }

   override def createAccount(username: String, password: String): IO[String] = {
      val generatedId = for {
         id <- IO.randomUUID
         securePassword = crypto.encode(password)
         _ <- insert_(id.toString(), username, securePassword).run.transact(xa)
      } yield id.toString()

      generatedId.orRaise(InvalidUsername(username))
   }
}

object AccountSql {

   def select(username: String): Query0[Account] = sql"""
        select
            username,
            password
        from account
        where username = ${username.toLowerCase()}
    """.query

   def insert_(id: String, username: String, securePassword: String): Update0 = sql"""
      insert into account (id, username, password) 
      values
      ($id, ${username.toLowerCase()}, $securePassword)
    """.update
}
