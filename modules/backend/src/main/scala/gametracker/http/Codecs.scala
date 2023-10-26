package gametracker.http

import gametracker.domain.*

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

private object Codecs {
   // Player Codecs
   given playerDecoder: Decoder[Player] = deriveDecoder[Player]
   given playerEncoder: Encoder[Player] = deriveEncoder[Player]
   given playerParamDecoder: Decoder[PlayerParam] = deriveDecoder[PlayerParam]

   // Game Codecs
   given gameDecoder: Decoder[Game] = deriveDecoder[Game]
   given gameEncoder: Encoder[Game] = deriveEncoder[Game]
   given gameParamDecoder: Decoder[GameParam] = deriveDecoder[GameParam]

   // Match Codecs
   given matchDecoder: Decoder[Match] = deriveDecoder[Match]
   given matchEncoder: Encoder[Match] = deriveEncoder[Match]
}
