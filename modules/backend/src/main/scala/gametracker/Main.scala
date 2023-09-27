package gametracker

import gametracker.modules.HttpApi

import cats.effect.{IO, IOApp, ExitCode}
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s.{ipv4, port}
import org.typelevel.log4cats.{LoggerFactory, slf4j}
import doobie.util.transactor.Transactor
import fly4s.core.Fly4s
import fly4s.implicits.{given}
import fly4s.core.data.{Fly4sConfig, Locations, ValidatePattern}
import doobie.util.log.LogHandler
import doobie.util.log.LogEvent

object Main extends IOApp {

   given LoggerFactory[IO] = slf4j.Slf4jFactory.create[IO]

   val xa = Transactor.fromDriverManager[IO](
     driver = "org.sqlite.JDBC",
     url = "jdbc:sqlite:/workspaces/gametracker/testing.db",
     logHandler = Some(
       new LogHandler[IO] {
          def run(logEvent: LogEvent): IO[Unit] = IO { println(logEvent.sql) }
       }
     )
   )

   val httpApi = HttpApi(xa)

   val server = EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApi.httpApp)
      .build

   // val server = for {
   //    service <- httpApi.httpApp.toResource
   //    s <- EmberServerBuilder
   //       .default[IO]
   //       .withHost(ipv4"0.0.0.0")
   //       .withPort(port"8080")
   //       .withHttpApp(service)
   //       .build
   // } yield s

   val fly4sRes = Fly4s.make[IO](
     url = "jdbc:sqlite:/workspaces/gametracker/testing.db",
     user = None,
     password = None,
     config = Fly4sConfig(
       table = "flyway",
       locations = Locations(List("db")),
       ignoreMigrationPatterns = List(ValidatePattern.ignorePendingMigrations)
     )
   )

   override def run(args: List[String]): IO[ExitCode] = {
      for {
         _      <- IO.println("----- Starting -----")
         result <- fly4sRes.evalMap(_.validateAndMigrate.result).use(IO(_))
         _      <- server.use(_ => IO.never)
         _      <- IO.println("----- Complete -----")
      } yield ExitCode.Success
   }
}