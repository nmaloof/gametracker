package gametracker.backend.services

import gametracker.backend.algebras.TokenAlg

import cats.effect.IO
import io.circe.syntax.*
import dev.profunktor.auth.jwt.{JwtSecretKey, JwtToken, jwtEncode}
import pdi.jwt.{JwtClaim, JwtAlgorithm}

class Token(secretKey: String) extends TokenAlg {

   given java.time.Clock = java.time.Clock.systemUTC()

   override def create: IO[JwtToken] = for {
      uuid  <- IO.randomUUID
      claim <- IO { JwtClaim(uuid.asJson.noSpaces).issuedNow.expiresIn(1 * 60) }
      secret = JwtSecretKey(secretKey)
      token <- jwtEncode[IO](claim, secret, JwtAlgorithm.HS256)
   } yield token
}
