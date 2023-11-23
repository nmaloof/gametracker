package gametracker.shared.domain

import munit.FunSuite

class MatchTests extends munit.FunSuite {

   val pingMatches1 = List(
     Match(1, Game(1, "PingPong"), Player(1, "Bob"), 1, 21),
     Match(1, Game(1, "PingPong"), Player(2, "Jill"), 2, 17)
   )
   val pingMatches2 = List(
     Match(2, Game(1, "PingPong"), Player(1, "Bob"), 1, 21),
     Match(2, Game(1, "PingPong"), Player(3, "Tiff"), 1, 21),
     Match(2, Game(1, "PingPong"), Player(2, "Jill"), 2, 17),
     Match(2, Game(1, "PingPong"), Player(4, "Bill"), 2, 17)
   )
   val tennisMatches = List(
     Match(3, Game(2, "Tennis"), Player(1, "Bob"), 1, 21),
     Match(3, Game(2, "Tennis"), Player(2, "Jill"), 2, 17)
   )

   test("MatchView - from one match (1 vs 1)") {
      val result = MatchView.from(pingMatches1)
      val expected = MatchView(
        1,
        Game(1, "PingPong"),
        Map(
          1 -> (21, List(Player(1, "Bob"))),
          2 -> (17, List(Player(2, "Jill")))
        )
      )
      assert(result.size == 1)
      assertEquals(result.head, expected)
   }

   test("MatchView - from one match (m vs n)") {
      val result = MatchView.from(pingMatches2)
      val expected = MatchView(
        2,
        Game(1, "PingPong"),
        Map(
          1 -> (21, List(Player(1, "Bob"), Player(3, "Tiff"))),
          2 -> (17, List(Player(2, "Jill"), Player(4, "Bill")))
        )
      )
      assert(result.size == 1)
      assertEquals(result.head, expected)
   }

   test("MatchView - from multiple match (m vs n)") {
      val result = MatchView.from(pingMatches1 ++ tennisMatches)
      assert(result.size == 2)
   }
}
