import org.scalajs.linker.interface.OutputPatterns
import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version      := "0.1.0-SNAPSHOT"

ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies ++= Seq(
  "org.typelevel" %% "typelevel-scalafix" % "0.2.0"
)

lazy val root = project
   .in(file("."))
   .settings(
     name := "gametracker"
   )
   .aggregate(backend, frontend, shared.jvm, shared.js)

lazy val shared = crossProject(JVMPlatform, JSPlatform)
   .crossType(CrossType.Pure)
   .in(file("modules/shared"))
   .settings(
     libraryDependencies ++= Seq(
       "org.scalameta" %%% "munit" % "1.0.0-M10" % Test
     )
   )

lazy val backend = project
   .in(file("modules/backend"))
   .settings(
     libraryDependencies ++= Seq(
       "org.typelevel"               %% "cats-core"              % "2.9.0",
       "org.typelevel"               %% "cats-effect"            % "3.5.1",
       "org.http4s"                  %% "http4s-ember-server"    % "0.23.23",
       "org.http4s"                  %% "http4s-dsl"             % "0.23.23",
       "org.http4s"                  %% "http4s-circe"           % "0.23.23",
       "dev.profunktor"              %% "http4s-jwt-auth"        % "1.2.1",
       "io.circe"                    %% "circe-core"             % "0.14.6",
       "io.circe"                    %% "circe-generic"          % "0.14.6",
       "com.github.jwt-scala"        %% "jwt-circe"              % "9.4.5",
       "org.typelevel"               %% "log4cats-slf4j"         % "2.6.0",
       "ch.qos.logback"               % "logback-classic"        % "1.4.14",
       "is.cir"                      %% "ciris"                  % "3.5.0",
       "is.cir"                      %% "ciris-http4s"           % "3.5.0",
       "org.tpolecat"                %% "doobie-core"            % "1.0.0-RC4",
       "com.github.geirolz"          %% "fly4s-core"             % "0.0.19",
       "org.xerial"                   % "sqlite-jdbc"            % "3.43.0.0",
       "org.springframework.security" % "spring-security-crypto" % "6.2.1",
       "org.slf4j"                    % "jcl-over-slf4j"         % "2.0.11",
       "org.bouncycastle"             % "bcprov-jdk18on"         % "1.77",
       "dev.profunktor"              %% "redis4cats-effects"     % "1.5.2",
       "dev.profunktor"              %% "redis4cats-log4cats"    % "1.5.2",
       "org.scalameta"               %% "munit"                  % "1.0.0-M10" % Test,
       "org.typelevel"               %% "munit-cats-effect"      % "2.0.0-M3"  % Test,
       "org.tpolecat"                %% "doobie-munit"           % "1.0.0-RC4" % Test
     )
   )
   .dependsOn(shared.jvm)

lazy val frontend = project
   .in(file("modules/frontend"))
   .enablePlugins(ScalaJSPlugin)
   .settings(
     libraryDependencies ++= Seq(
       "org.scala-js"  %%% "scalajs-dom"   % "2.8.0",
       "com.raquo"     %%% "laminar"       % "16.0.0",
       "io.laminext"   %%% "fetch"         % "0.16.2",
       "io.laminext"   %%% "fetch-circe"   % "0.16.2",
       "io.circe"      %%% "circe-core"    % "0.14.6",
       "io.circe"      %%% "circe-generic" % "0.14.6",
       "com.raquo"     %%% "waypoint"      % "7.0.0",
       "org.scalameta" %%% "munit"         % "1.0.0-M10" % Test

       // "org.http4s" %%% "http4s-dom" % "0.2.9",
       // "org.http4s" %%% "http4s-ember-client" % "0.23.23",
     ),
     scalaJSUseMainModuleInitializer := true,
     scalaJSLinkerConfig ~= {
        _.withModuleKind(ModuleKind.ESModule)
           .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
           .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
     },
     // https://github.com/com-lihaoyi/mill/issues/2300
     Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
     Test / scalaJSLinkerConfig ~= {
        _.withModuleKind(ModuleKind.NoModule)
           .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
           .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
     }
   )
   .dependsOn(shared.js)
