addSbtPlugin("org.scalameta"      % "sbt-scalafmt"             % "2.4.6")
addSbtPlugin("ch.epfl.scala"      % "sbt-scalafix"             % "0.11.1")
addSbtPlugin("io.spray"           % "sbt-revolver"             % "0.10.0")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.15.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
