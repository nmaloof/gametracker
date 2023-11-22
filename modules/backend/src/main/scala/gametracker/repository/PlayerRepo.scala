package gametracker.backend.repository

import gametracker.backend.algebras.PlayerAlg
import gametracker.shared.domain.*

import cats.data.OptionT
import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*

class PlayerRepo(xa: Transactor[IO]) extends PlayerAlg {

   import PlayerSQL.*

   override def findById(id: Long): OptionT[IO, Player] = OptionT(select(id).option.transact(xa))

   override def findByName(name: String): OptionT[IO, Player] = OptionT(select(name).option.transact(xa))

   override def delete(): IO[Unit] = ???

   override def insert(player: PlayerParam): IO[Either[Error, Unit]] = insert_(player).run.void.attemptSql
      .map { f =>
         f.leftMap(ex => Error(ex.getMessage()))
      }
      .transact(xa)

   override def findGamesPlayed(id: Long): IO[List[Game]] = findGames(id).to[List].transact(xa)

}

private object PlayerSQL {
   def select(id: Long): Query0[Player] = sql"select id, username from player where id = $id".query

   def select(name: String): Query0[Player] = sql"select id, username from player where name = $name".query

   def delete_(): Update0 = sql"delete from player where id =${}".update

   def insert_(player: PlayerParam): Update0 = sql"insert into player (username) values (${player.username})".update

   def findGames(id: Long): Query0[Game] = sql"""
      select distinct
         g.id,
         g.name 
      from match as m 
      left join match_detail as md
         on (m.id = md.match_id)
      left join game as g
         on (g.id = m.game_id)
      where md.player_id = $id
   """.query
}
