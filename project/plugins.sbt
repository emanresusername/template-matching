Seq(
  "com.eed3si9n"     % "sbt-assembly"    % "0.14.3",
  "com.vmunier"      % "sbt-web-scalajs" % "1.0.3",
  "org.scala-js"     % "sbt-scalajs"     % "0.6.13",
  "io.spray"         % "sbt-revolver"    % "0.8.0",
  "com.typesafe.sbt" % "sbt-twirl"       % "1.3.0",
  "com.geirsson"     % "sbt-scalafmt"    % "0.5.0-RC3"
).map(addSbtPlugin)
