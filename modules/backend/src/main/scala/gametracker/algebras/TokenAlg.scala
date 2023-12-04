package gametracker.backend.algebras

import cats.effect.IO
import dev.profunktor.auth.jwt
import dev.profunktor.auth.jwt.{JwtSecretKey, JwtToken}
import io.circe.syntax.*
import pdi.jwt.*

trait TokenAlg {
   def create: IO[JwtToken]
}

class Token(secretKey: String) extends TokenAlg {
   given java.time.Clock = java.time.Clock.systemUTC()

   override def create: IO[JwtToken] = for {
      uuid <- IO.randomUUID
      claim <- IO.apply {
         JwtClaim(uuid.asJson.noSpaces).issuedNow.expiresIn(15 * 60)
      }
      secret = JwtSecretKey(secretKey)
      token <- jwt.jwtEncode[IO](claim, secret, JwtAlgorithm.HS256)
   } yield token
}
