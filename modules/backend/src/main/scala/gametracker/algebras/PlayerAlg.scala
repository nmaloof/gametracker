package gametracker.algebras

import gametracker.domain.Player

import cats.effect.IO
import cats.data.OptionT

trait PlayerAlg {
   def findById(id: Long): OptionT[IO, Player]

   def delete(): IO[Unit]

   def insert(player: Player): IO[Either[Error, Unit]]
}
