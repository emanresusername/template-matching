scalaVersion := "2.11.8"
organization := "my.will.be.done"
name := "template-matching"
version := "0.3.1"

libraryDependencies ++= {
  val boofcvVersion = "0.24.1"
  val boofcvOrg = "org.boofcv"
  val circeVersion = "0.6.1"

  Seq(
    boofcvOrg            % "core"             % boofcvVersion,
    "com.typesafe.akka" %% "akka-http"        % "10.0.0",
    "de.heikoseeberger" %% "akka-http-circe"  % "1.11.0",
    "io.circe"          %% "circe-generic"    % circeVersion
  )
}

mainClass in assembly := Some("my.will.be.done.templatematching.WebServer")

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         ⇒
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" ⇒
    MergeStrategy.first
  case "application.conf"                            ⇒
    MergeStrategy.concat
  case PathList("org", "xmlpull", "v1", xs @ _*) ⇒
    MergeStrategy.first
  case x ⇒
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

import sbtassembly.AssemblyPlugin.defaultShellScript

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"${name.value}-${version.value}"
