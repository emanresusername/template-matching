scalaVersion := "2.11.8"
organization := "practechal.me"
name := "template-matching"
version := "0.1.0"

libraryDependencies ++= {
  val boofcvVersion = "0.24.1"
  val boofcvOrg = "org.boofcv"
  Seq(
    boofcvOrg % "core" % boofcvVersion
  )
}

mainClass in assembly := Some("me.practechal.templatematching.ImageTester")

assemblyMergeStrategy in assembly := {
  case PathList("org", "xmlpull", "v1", xs @ _*) ⇒
    MergeStrategy.first
  case x ⇒
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

import sbtassembly.AssemblyPlugin.defaultShellScript

assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))

assemblyJarName in assembly := s"${name.value}-${version.value}"
