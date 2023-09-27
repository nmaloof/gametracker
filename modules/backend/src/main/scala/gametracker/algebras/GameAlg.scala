package gametracker.algebras

import gametracker.domain.Game

import cats.data.OptionT
import cats.effect.IO

trait GameAlg {
   def findById(id: Long): OptionT[IO, Game]

   def findAll(): IO[List[Game]]

   def insert(game: Game): IO[Either[Error, Unit]]

   def delete(id: Long): IO[Unit]
}
