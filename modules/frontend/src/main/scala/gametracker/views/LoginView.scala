package gametracker.frontend.views

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object LoginView {

   val username = Var("")
   val password = Var("")

   def render(): Element = {
      form(
        div(
          label(
            "Username",
            input(
              `type`      := "text",
              placeholder := "Enter Username",
              required    := true,
              onInput.mapToValue --> username
            )
          ),
          label(
            "Password",
            input(
              `type`      := "password",
              placeholder := "Enter Password",
              required    := true,
              onInput.mapToValue --> password
            )
          ),
          button("Login")
        )
      )
   }

   // val sendLogin = onClick.preventDefault --> { _ =>
   //     Fetch
   // }
}
