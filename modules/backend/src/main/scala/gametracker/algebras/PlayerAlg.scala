package gametracker.algebras

import gametracker.domain.Player

import cats.data.OptionT
import cats.effect.IO

trait PlayerAlg {
   def findById(id: Long): OptionT[IO, Player]

   def delete(): IO[Unit]

   def insert(player: Player): IO[Either[Error, Unit]]
}
