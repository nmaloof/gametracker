import org.scalajs.linker.interface.OutputPatterns
import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "3.3.0"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies ++= Seq(
    "org.typelevel" %% "typelevel-scalafix" % "0.2.0"
)


lazy val root = project.in(file(".")).aggregate(backend, frontend)


lazy val backend = project
    .in(file("modules/backend"))
    .settings(
        libraryDependencies ++= Seq(
            "org.typelevel" %% "cats-core" % "2.9.0",
            "org.typelevel" %% "cats-effect" % "3.5.1",

            "org.http4s" %% "http4s-ember-server" % "0.23.23",
            "org.http4s" %% "http4s-dsl" % "0.23.23",
            "org.http4s" %% "http4s-circe" % "0.23.23",
        
            "io.circe" %% "circe-core" % "0.14.6",
            "io.circe" %% "circe-generic" % "0.14.6",

            "org.typelevel" %% "log4cats-slf4j" % "2.6.0",

            "org.tpolecat" %% "doobie-core" % "1.0.0-RC4",
            "com.github.geirolz" %% "fly4s-core" % "0.0.19",
            "org.xerial" % "sqlite-jdbc" % "3.43.0.0",

            "org.scalameta" %% "munit" % "1.0.0-M10" % Test,
            "org.typelevel" %% "munit-cats-effect" % "2.0.0-M3" % Test,
            "org.tpolecat" %% "doobie-munit" % "1.0.0-RC4" % Test,

        )
    )

lazy val frontend = project
    .in(file("modules/frontend"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
        libraryDependencies ++= Seq(
            "org.scala-js" %%% "scalajs-dom" % "2.7.0",

            "com.raquo" %%% "laminar" % "16.0.0",

            "io.laminext" %%% "fetch" % "0.16.2",
            "io.laminext" %%% "fetch-circe" % "0.16.2",

            "io.circe" %%% "circe-core" % "0.14.6",
            "io.circe" %%% "circe-generic" % "0.14.6",


            // "org.http4s" %%% "http4s-dom" % "0.2.9",
            // "org.http4s" %%% "http4s-ember-client" % "0.23.23",
        ),
        scalaJSUseMainModuleInitializer := true,
        scalaJSLinkerConfig ~= {
            _.withModuleKind(ModuleKind.ESModule)
                .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
                .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
        }
    )