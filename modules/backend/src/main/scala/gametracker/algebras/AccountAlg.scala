package gametracker.backend.algebras

import gametracker.backend.domain.Account

import cats.data.OptionT
import cats.effect.IO

trait AccountAlg {

   /** Finds the account with the given username and password
     *
     * @param username
     *   Username to search for
     * @param password
     *   Password to match if the username exists
     * @return
     *   [[Acount]]
     */
   def findAccount(username: String, password: String): OptionT[IO, Account]

   /** Creates a new account
     *
     * @param username
     *   New accounts username
     * @param password
     *   New accounts password
     * @return
     *   New accounts generated id as a String
     */
   def createAccount(username: String, password: String): IO[String]
}
