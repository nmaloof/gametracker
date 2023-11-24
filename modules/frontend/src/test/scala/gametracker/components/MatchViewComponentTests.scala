package gametracker.components

// import gametracker.components.*

import munit.*

class MatchViewComponentTests extends FunSuite {

   test("Component Render") {
      val player = MatchViewComponent.Player(1, "Bill")
      val game   = MatchViewComponent.Game(1, "PingPong")

      val matchView = MatchViewComponent.MatchView(
        1,
        game,
        Map(
          1 -> (21, List(player, player)),
          2 -> (17, List(player, player))
        )
      )

      val result = MatchViewComponent.render(matchView = matchView)
      println(result.toString())
      println("################")
      assert(true)
   }
}
