package gametracker.backend

import gametracker.backend.config.AppConfig
import gametracker.backend.modules.HttpApi

import cats.effect.{ExitCode, IO, IOApp, Resource}
import dev.profunktor.redis4cats.log4cats.*
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import doobie.util.log.{LogEvent, LogHandler}
import doobie.util.transactor.Transactor
import fly4s.core.Fly4s
import fly4s.core.data.{Fly4sConfig, Locations, ValidatePattern}
import fly4s.implicits.{given}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{Logger, LoggerFactory, slf4j}

object Main extends IOApp {

   given LoggerFactory[IO]  = slf4j.Slf4jFactory.create[IO]
   given logger: Logger[IO] = LoggerFactory[IO].getLogger

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

   def makeRedis(config: AppConfig): Resource[IO, RedisCommands[IO, String, String]] = Redis[IO].utf8(config.redisConfig.url)

   override def run(args: List[String]): IO[ExitCode] = {
      for {
         _      <- logger.info("----- Starting -----")
         config <- AppConfig.config.load[IO]
         fly4sRes = makeFlyway(config)
         result <- fly4sRes.evalMap(_.validateAndMigrate.result).use(IO(_))
         xa = makeTransactor(config)
         _ <- makeRedis(config).use { redis =>
            val httpApi = HttpApi(xa, config, redis)
            val server = EmberServerBuilder
               .default[IO]
               .withHost(config.apiConfig.host)
               .withPort(config.apiConfig.port)
               .withHttpApp(httpApi.httpApp)
               .build
            server.use(_ => IO.never)
         }
         _ <- logger.info("----- Complete -----")
      } yield ExitCode.Success
   }
}
