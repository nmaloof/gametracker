package gametracker.backend.algebras

import cats.effect.IO
import dev.profunktor.auth.jwt.JwtToken

trait TokenAlg {

   /** Creates a JWT
     *
     * @return
     *   Generated JWT
     */
   def create: IO[JwtToken]
}
