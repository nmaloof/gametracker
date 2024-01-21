package gametracker.frontend.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.*

final case class Account(
    username: String,
    password: String
)

given Decoder[Account] = deriveDecoder[Account]
given Encoder[Account] = deriveEncoder[Account]
