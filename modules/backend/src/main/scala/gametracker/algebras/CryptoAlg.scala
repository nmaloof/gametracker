package gametracker.backend.algebras

trait CryptoAlg {

   /** Encodes a string
     *
     * @param value
     *   Raw "unsecure" string
     * @return
     *   Encoded string
     */
   def encode(value: String): String

   /** Decodes a string
     *
     * @param value
     *   Encoded String
     * @return
     *   Raw "unsecure" string
     */
   def decode(value: String): String

   /** Checks if a non-encoded string matches its encoded version
     *
     * @param raw
     *   Raw "unsecure" string
     * @param encoded
     *   Encoded String
     * @return
     *   True if they are equal
     */
   def matches(raw: String, encoded: String): Boolean
}
