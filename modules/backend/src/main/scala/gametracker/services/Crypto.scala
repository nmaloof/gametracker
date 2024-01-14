package gametracker.backend.services

import gametracker.backend.algebras.CryptoAlg

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

class Crypto extends CryptoAlg {

   private val argon = new Argon2PasswordEncoder(saltLength = 16, hashLength = 64, parallelism = 1, memory = 60000, iterations = 2)

   override def encode(value: String): String                  = argon.encode(value)
   
   override def decode(value: String): String                  = ???
   
   override def matches(raw: String, encoded: String): Boolean = argon.matches(raw, encoded)
}
