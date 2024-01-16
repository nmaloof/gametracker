package gametracker.backend.repository

import gametracker.backend.algebras.PlayerAlg
import gametracker.backend.domain.PlayerAlreadyExists
import gametracker.shared.domain.*

import cats.data.OptionT
import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*

class PlayerRepo(xa: Transactor[IO]) extends PlayerAlg {

   import PlayerSQL.*

   override def findAll(): IO[List[Player]] = baseSelect.query[Player].to[List].transact(xa)

   override def findById(id: Long): OptionT[IO, Player] = OptionT(select(id).option.transact(xa))

   override def findByName(name: String): OptionT[IO, Player] = OptionT(select(name.toLowerCase).option.transact(xa))

   override def delete(): IO[Unit] = ???

   override def insert(player: PlayerParam): IO[Unit] = insert_(player).run.void.transact(xa).orRaise(PlayerAlreadyExists(player.name))

   override def findGamesPlayed(id: Long): IO[List[Game]] = findGames(id).to[List].transact(xa)

}

private object PlayerSQL {

   val baseSelect = fr"select id, name from player"

   def select(id: Long): Query0[Player] = (baseSelect ++ fr"where id = $id").query

   def select(name: String): Query0[Player] = sql"select id, name from player where name = $name".query

   def delete_(): Update0 = sql"delete from player where id =${}".update

   def insert_(player: PlayerParam): Update0 = sql"insert into player (name) values (${player.name.toLowerCase})".update

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
