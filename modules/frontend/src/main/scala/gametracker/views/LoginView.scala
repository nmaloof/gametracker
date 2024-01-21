package gametracker.frontend.views

import gametracker.frontend.MainPage
import gametracker.frontend.router
import gametracker.frontend.domain.{Account, given}

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.http4s.dom.*
import cats.effect.IO
import org.http4s.*
import org.http4s.implicits.uri
import org.http4s.circe.CirceEntityCodec.*
import cats.effect.unsafe.implicits.*
import org.http4s.headers

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
          button("Login", sendLogin)
        )
      )
   }

   val sendLogin = onClick.preventDefault --> { _ =>
      fetchToken.unsafeRunAndForget()
   }

   private val fetchToken = {
      val client = FetchClientBuilder[IO].create
      for {
         _ <- IO.println("Fetching token")
         token <- client
            .expect[String](
              Request(method = Method.POST, uri = uri"http://localhost:8080/auth/login")
                 .withEntity(
                   Account(username.now(), password.now())
                 )
            )
            .attempt
         _ <- IO {
            token match
               case Left(value)  => println("Failed to login")
               case Right(value) => router.pushState(MainPage)

         }
      } yield token
   }
}
