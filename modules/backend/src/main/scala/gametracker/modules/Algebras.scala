package gametracker.backend.modules

import gametracker.backend.repository.*

import cats.effect.IO
import doobie.Transactor

final class Algebras(xa: Transactor[IO]) {

   val games    = new GameRepo(xa)
   val players  = new PlayerRepo(xa)
   val matches  = new MatchRepo(xa)
   val accounts = new AccountRepo(xa)

}
