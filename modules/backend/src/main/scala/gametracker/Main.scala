package gametracker.backend

import gametracker.backend.config.AppConfig
import gametracker.backend.modules.HttpApi

import cats.effect.{ExitCode, IO, IOApp}
import doobie.util.log.{LogEvent, LogHandler}
import doobie.util.transactor.Transactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Locations, ValidatePattern}
import fly4s.implicits.{given}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{LoggerFactory, slf4j}

object Main extends IOApp {

   given LoggerFactory[IO] = slf4j.Slf4jFactory.create[IO]
   val logger              = LoggerFactory[IO].getLogger

   def makeTransactor(config: AppConfig): Transactor[IO] = {
      Transactor.fromDriverManager[IO](
        driver = "org.sqlite.JDBC",
        url = config.databaseConfig.url,
        logHandler = Some(
          new LogHandler[IO] {
             def run(logEvent: LogEvent): IO[Unit] = logger.debug(logEvent.sql) // IO { println(logEvent.sql) }
          }
        )
      )
   }

   def makeFlyway(config: AppConfig) = Fly4s.make[IO](
     url = config.databaseConfig.url,
     user = None,
     password = None,
     config = Fly4sConfig(
       table = "flyway",
       locations = Locations(List("db")),
       ignoreMigrationPatterns = List(ValidatePattern.ignorePendingMigrations)
     )
   )

   // val server = for {
   //    service <- httpApi.httpApp.toResource
   //    s <- EmberServerBuilder
   //       .default[IO]
   //       .withHost(ipv4"0.0.0.0")
   //       .withPort(port"8080")
   //       .withHttpApp(service)
   //       .build
   // } yield s

   override def run(args: List[String]): IO[ExitCode] = {
      for {
         _      <- logger.info("----- Starting -----")
         config <- AppConfig.config.load[IO]
         fly4sRes = makeFlyway(config)
         result <- fly4sRes.evalMap(_.validateAndMigrate.result).use(IO(_))
         xa      = makeTransactor(config)
         httpApi = HttpApi(xa)
         server = EmberServerBuilder
            .default[IO]
            .withHost(config.apiConfig.host)
            .withPort(config.apiConfig.port)
            .withHttpApp(httpApi.httpApp)
            .build
         _ <- server.use(_ => IO.never)
         _ <- logger.info("----- Complete -----")
      } yield ExitCode.Success
   }
}
