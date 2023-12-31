package gametracker.backend.algebras

import gametracker.shared.domain.*

import cats.data.OptionT
import cats.effect.IO

trait PlayerAlg {
   def findById(id: Long): OptionT[IO, Player]

   def findByName(name: String): OptionT[IO, Player]

   def findGamesPlayed(id: Long): IO[List[Game]]

   def delete(): IO[Unit]

   def insert(player: PlayerParam): IO[Either[Error, Unit]]

}
