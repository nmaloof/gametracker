package gametracker.backend.algebras

import gametracker.shared.domain.*

import cats.data.OptionT
import cats.effect.IO

trait GameAlg {
   def findById(id: Long): OptionT[IO, Game]

   def findAll(): IO[List[Game]]

   def insert(game: GameParam): IO[Unit]

   def delete(id: Long): IO[Unit]
}
