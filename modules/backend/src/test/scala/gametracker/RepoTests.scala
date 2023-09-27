package gametracker

import gametracker.domain.*
import gametracker.repository.MatchSQL
import gametracker.repository.MatchRepo

import munit.*
import cats.effect.IO
import doobie.Transactor

class RepoTests extends CatsEffectSuite with doobie.munit.IOChecker {

   val transactor = Transactor.fromDriverManager[IO](
     driver = "org.sqlite.JDBC",
     url = "jdbc:sqlite:/workspaces/gametracker/testing.db",
     logHandler = None
   )

   test("Match Type Checks") {
      check(MatchSQL.baseSelect.query[Match])
   }

   // test("Match Type Results") {
   //    val m = MatchRepo(transactor)
   //    for {
   //       res <- m.findById(10).value
   //       _   <- IO.println(res)
   //    } yield res
   // }
}
