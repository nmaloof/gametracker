package gametracker.repository

import gametracker.algebras.MatchAlg
import gametracker.domain.Match

import cats.data.OptionT
import cats.effect.IO
import doobie.*
import doobie.implicits.*

import Fragments.whereAndOpt

class MatchRepo(xa: Transactor[IO]) extends MatchAlg {
   import MatchSQL.*

   override def findAll(): IO[List[Match]] = baseSelect.query[Match].to[List].transact(xa)

   override def findById(id: Long): OptionT[IO, List[Match]] = OptionT(
     select(id).to[List].map(x => if x.isEmpty then None else Some(x)).transact(xa)
   )

   override def findBy(playerId: Option[Long], gameId: Option[Long]): OptionT[IO, List[Match]] = OptionT(
     filter(playerId, gameId).to[List].map(x => if x.isEmpty then None else Some(x)).transact(xa)
   )
}

object MatchSQL {

   val baseSelect = fr"""
      select
         m.id,
         g.id,
         g.name,
         p.id,
         p.username,
         md.team_id,
         md.score
      from match as m
      join game as g
         on g.id = m.game_id
      join match_detail as md
         on md.match_id = m.id
      join player as p
         on p.id = md.player_id
   """

   def select(id: Long): Query0[Match] = (baseSelect ++ fr"where m.id = $id").query

   def filter(playerId: Option[Long], gameId: Option[Long]): Query0[Match] = {
      val f1 = playerId.map(i => fr"m.id in (select distinct match_id from match_detail where player_id = $i)")
      val f2 = gameId.map(i => fr"g.id = $i")
      (baseSelect ++ whereAndOpt(f1, f2)).query
   }
}
