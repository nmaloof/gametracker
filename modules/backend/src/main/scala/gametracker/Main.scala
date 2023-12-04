package gametracker.backend

import gametracker.backend.modules.HttpApi

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{ipv4, port}
import doobie.util.log.{LogEvent, LogHandler}
import doobie.util.transactor.Transactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Locations, ValidatePattern}
import fly4s.implicits.{given}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{LoggerFactory, slf4j}

object Main extends IOApp {

   given LoggerFactory[IO] = slf4j.Slf4jFactory.create[IO]

   // val dbUrl = "jdbc:sqlite:/Users/nmaloof/Documents/Software/gametracker/testing.db"  // Mac
   val dbUrl = "jdbc:sqlite:/workspaces/gametracker/testing.db" // Windows
   val xa = Transactor.fromDriverManager[IO](
     driver = "org.sqlite.JDBC",
     url = dbUrl,
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
     url = dbUrl,
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
