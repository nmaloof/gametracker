package gametracker.algebras

import gametracker.domain.{Game, GameParam}

import cats.data.OptionT
import cats.effect.IO

trait GameAlg {
   def findById(id: Long): OptionT[IO, Game]

   def findAll(): IO[List[Game]]

   def insert(game: GameParam): IO[Either[Error, Unit]]

   def delete(id: Long): IO[Unit]
}
