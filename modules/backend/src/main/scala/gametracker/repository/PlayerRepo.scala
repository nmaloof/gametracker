package gametracker.repository

import gametracker.algebras.PlayerAlg
import gametracker.domain.Player

import cats.data.OptionT
import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*

class PlayerRepo(xa: Transactor[IO]) extends PlayerAlg {

   import PlayerSQL.*

   override def findById(id: Long): OptionT[IO, Player] = OptionT(select(id).option.transact(xa))

   override def delete(): IO[Unit] = ???

   override def insert(player: Player): IO[Either[Error, Unit]] = insert_(player).run.void.attemptSql
      .map { f =>
         f.leftMap(ex => Error(ex.getMessage()))
      }
      .transact(xa)

}

private object PlayerSQL {
   def select(id: Long): Query0[Player] = sql"select id, username from player where id = $id".query

   def delete_(): Update0 = sql"delete from player where id =${}".update

   def insert_(player: Player): Update0 = sql"insert into player (id, username) values (${player.id}, ${player.username})".update
}
