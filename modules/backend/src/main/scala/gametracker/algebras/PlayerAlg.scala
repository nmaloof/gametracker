package gametracker.algebras

import gametracker.domain.Player
import gametracker.domain.Game

import cats.data.OptionT
import cats.effect.IO

trait PlayerAlg {
   def findById(id: Long): OptionT[IO, Player]

   def findByName(name: String): OptionT[IO, Player]

   def findGamesPlayed(id: Long): IO[List[Game]]

   def delete(): IO[Unit]

   def insert(player: Player): IO[Either[Error, Unit]]

}
