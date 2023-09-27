package gametracker.algebras

import gametracker.domain.Match

import cats.effect.IO
import cats.data.OptionT

trait MatchAlg {
   def findById(id: Long): OptionT[IO, List[Match]]

   def findAll(): IO[List[Match]]

   def findBy(playerId: Option[Long], gameId: Option[Long]): OptionT[IO, List[Match]] // TODO: This list could be empty. Maybe use strings?
}
