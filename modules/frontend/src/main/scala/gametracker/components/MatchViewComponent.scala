package gametracker.frontend.components

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import gametracker.shared.domain.*

object MatchViewComponent {

   def render(matchViews: Signal[List[MatchView]]): Element = {
      div(
        children <-- matchViews.map(mv => mv.map(renderOneMatch))
      )
   }

   def renderOneMatch(matchView: MatchView): Div = {
      div(
        cls := "match",
        div(
          cls := "game",
          matchView.game.name
        ),
        renderTeamDetails(matchView.teamDetails)
      )
   }

   def renderTeamDetails(teamDetails: TeamDetails): List[Div] = {
      val orderedDetails = teamDetails.toList.sortBy(x => x._2._1).reverse
      val topTeamScore   = orderedDetails.head._2._1

      val elements = for {
         td <- teamDetails
      } yield {
         div(
           cls := "team",
           div(
             cls := "player",
             td._2._2.map(x => p(x.username))
           ),
           div(
             if td._2._1 == topTeamScore then {
                cls := "score winner"
             } else {
                cls := "score loser"
             },
             td._2._1
           )
         )
      }
      elements.toList
   }
}
