package gametracker.backend.config

import cats.syntax.all.*
import ciris.*
import ciris.http4s.*
import com.comcast.ip4s.*

final case class AppConfig(
    apiConfig: ApiConfig,
    databaseConfig: DatabaseConfig,
    redisConfig: RedisConfig,
    securityConfig: SecurityConfig
)

final case class DatabaseConfig(url: String, username: String, password: String)
final case class RedisConfig(url: String)

final case class ApiConfig(
    host: Host,
    port: Port
)

final case class SecurityConfig(
    secretKey: String
)

object AppConfig {

   val databaseConfig: ConfigValue[Effect, DatabaseConfig] = {
      (
        env("DB_URL").default("jdbc:postgresql://localhost:5432/gametracker"),
        env("DB_USERNAME").default("postgres"),
        env("DB_PASSWORD").default("secretpassword")
      ).parMapN(DatabaseConfig.apply)
   }

   val redisConfig: ConfigValue[Effect, RedisConfig] = {
      default("redis://localhost:6379").map(RedisConfig.apply)
   }

   val apiConfig: ConfigValue[Effect, ApiConfig] = {
      (
        env("API_HOST").or(prop("http4s.host")).as[Host].default(ipv4"0.0.0.0"),
        env("API_PORT").or(prop("http4s.port")).as[Port].default(port"8080")
      ).parMapN(ApiConfig.apply)
   }

   val securityConfig: ConfigValue[Effect, SecurityConfig] = {
      default("secret-key").map(SecurityConfig.apply)
   }

   def config: ConfigValue[Effect, AppConfig] = (apiConfig, databaseConfig, redisConfig, securityConfig).parMapN(AppConfig.apply)
}
