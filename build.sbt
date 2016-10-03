scalaVersion := "2.11.8"
organization := "practechal.me"
name := "template-matching"
version := "0.2.0"

libraryDependencies ++= {
  val boofcvVersion = "0.24.1"
  val boofcvOrg = "org.boofcv"
  val raptureVersion = "2.0.0-M7"
  val raptureOrg = "com.propensive"

  Seq(
    boofcvOrg  % "core"                % boofcvVersion,
    raptureOrg %% "rapture-http-jetty" % raptureVersion,
    raptureOrg %% "rapture-json-circe" % raptureVersion
  )
}

mainClass in assembly := Some("me.practechal.templatematching.Server")

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
