package gametracker.shared.domain

case class Match(
    id: Int,
    game: Game,
    player: Player,
    teamId: Int,
    score: Int
)

type TeamDetails = Map[Int, (Int, List[Player])]

final case class MatchView(
    id: Int,
    game: Game,
    teamDetails: TeamDetails
)

object MatchView {
   def from(matches: List[Match]): List[MatchView] = {
      val result = for {
         mg <- matches.groupBy(_.id)
      } yield {
         val _id     = mg._1
         val mats    = mg._2
         val details = mats.groupMapReduce(_.teamId)(m => (m.score, List(m.player)))((a, b) => (a._1, a._2 ++ b._2))
         MatchView(_id, mats.head.game, details)
      }
      result.toList
   }
}
