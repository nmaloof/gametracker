package gametracker.backend.repository

import gametracker.backend.algebras.AccountAlg
import gametracker.backend.domain.Account

import cats.data.OptionT
import cats.effect.IO
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor

class AccountRepo(xa: Transactor[IO]) extends AccountAlg {
   import AccountSql.*
   override def findAccount(username: String, password: String): OptionT[IO, Account] = {
      OptionT(select(username, password).option.transact(xa))
   }
   override def createAccount(username: String, password: String): IO[String] = ???
}

object AccountSql {

   // TODO: Change this to not store plain text
   def select(username: String, password: String): Query0[Account] = sql"""
        select
            username,
            password
        from account
        where username = $username
            and password = $password
    """.query
}
