package gametracker.backend.algebras

import gametracker.backend.domain.Account

import cats.effect.IO
import dev.profunktor.auth.jwt.JwtToken

trait AuthAlg {

   /** Logs In
     *
     * @param username
     *   Username corresponding to an [[gametracker.backend.domain.Account]]
     * @param password
     *   Password corresponding to an [[gametracker.backend.domain.Account]]
     * @return
     *   JWT
     */
   def login(username: String, password: String): IO[JwtToken]

   /** Logs out
     *
     * @param token
     *   JWT
     * @return
     */
   def logout(token: JwtToken): IO[Unit]
}
