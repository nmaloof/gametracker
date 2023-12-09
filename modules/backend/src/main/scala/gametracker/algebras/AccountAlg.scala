package gametracker.backend.algebras

import gametracker.backend.domain.Account

import cats.data.OptionT
import cats.effect.IO

trait AccountAlg {

   def findAccount(username: String, password: String): OptionT[IO, Account]

   def createAccount(username: String, password: String): IO[String]
}
