package gametracker.backend.config

import cats.syntax.all.*
import ciris.*
import ciris.http4s.*
import com.comcast.ip4s.*

final case class AppConfig(
    apiConfig: ApiConfig,
    databaseConfig: DatabaseConfig,
    securityConfig: SecurityConfig
)

final case class DatabaseConfig(url: String)

final case class ApiConfig(
    host: Host,
    port: Port
)

final case class SecurityConfig(
    secretKey: String
)

object AppConfig {

   val databaseConfig: ConfigValue[Effect, DatabaseConfig] = {
      env("DB_URL")
         .default("jdbc:sqlite:/workspaces/gametracker/testing.db")
         .map(DatabaseConfig.apply) // For Mac: "jdbc:sqlite:/Users/nmaloof/Documents/Software/gametracker/testing.db"
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

   def config: ConfigValue[Effect, AppConfig] = (apiConfig, databaseConfig, securityConfig).parMapN(AppConfig.apply)
}
