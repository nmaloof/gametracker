package gametracker.backend.modules

import gametracker.backend.repository.*
import gametracker.backend.services.*

import cats.effect.IO
import doobie.Transactor

final class Algebras(xa: Transactor[IO]) {

   val crypto   = new Crypto()
   val games    = new GameRepo(xa)
   val players  = new PlayerRepo(xa)
   val matches  = new MatchRepo(xa)
   val accounts = new AccountRepo(xa, crypto)

}
