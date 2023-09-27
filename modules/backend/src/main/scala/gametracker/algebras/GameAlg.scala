package gametracker.algebras

import gametracker.domain.Game

import cats.effect.IO
import cats.data.OptionT

trait GameAlg {
   def findById(id: Long): OptionT[IO, Game]

   def findAll(): IO[List[Game]]

   def insert(game: Game): IO[Either[Error, Unit]]

   def delete(id: Long): IO[Unit]
}
