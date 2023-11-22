package gametracker.backend.algebras

import gametracker.shared.domain.Match

import cats.data.OptionT
import cats.effect.IO

trait MatchAlg {
   def findById(id: Long): OptionT[IO, List[Match]]

   def findAll(): IO[List[Match]]

   def findBy(playerId: Option[Long], gameId: Option[Long]): OptionT[IO, List[Match]] // TODO: This list could be empty. Maybe use strings?
}
