package gametracker.backend.repository

import gametracker.shared.domain.*
import gametracker.backend.algebras.GameAlg

import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.implicits.*

class GameRepo(xa: Transactor[IO]) extends GameAlg {
   import GameSQL.*

   override def findById(id: Long): OptionT[IO, Game] = OptionT(select(id).option.transact(xa))

   override def findAll(): IO[List[Game]] = baseSelect.query[Game].to[List].transact(xa)

   override def insert(game: GameParam): IO[Either[Error, Unit]] = {
      insert_(game).run.void.attemptSql
         .map { f =>
            f.leftMap(ex => Error(ex.getMessage()))
         }
         .transact(xa)
   }

   override def delete(id: Long): IO[Unit] = delete_(id).run.transact(xa).void

}

private object GameSQL {
   val baseSelect = fr"select id, name from game"

   def select(id: Long): Query0[Game] = (baseSelect ++ fr"where id = $id").query

   def delete_(id: Long): Update0 = sql"delete from game where id=$id".update

   def insert_(game: GameParam): Update0 = sql"insert into game (name) values (${game.name})".update
}
