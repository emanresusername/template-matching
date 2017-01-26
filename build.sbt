lazy val commonSettings = Seq(
  organization := "my.will.be.done",
  version := "0.5.1",
  scalaVersion := "2.12.1"
)

import sbtassembly.AssemblyPlugin.defaultShellScript

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    name := "template-matching-server",
    libraryDependencies ++= {
      val boofcvVersion = "0.26"
      val boofcvOrg     = "org.boofcv"
      val circeVersion  = "0.7.0"

      Seq(
        boofcvOrg           % "core"             % boofcvVersion,
        "com.typesafe.akka" %% "akka-http"       % "10.0.3",
        "de.heikoseeberger" %% "akka-http-circe" % "1.12.0",
        "io.circe"          %% "circe-generic"   % circeVersion,
        "com.vmunier"       %% "scalajs-scripts" % "1.1.0",
        "btomala"           %% "akka-http-twirl" % "1.2.0"
      )
    },
    resolvers += "Bartek's repo at Bintray" at "https://dl.bintray.com/btomala/maven",
    mainClass in assembly := Some(
      "my.will.be.done.templatematching.server.TemplateMatchingServer"),
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _ *) ⇒
        MergeStrategy.first
      case PathList(ps @ _ *) if ps.last endsWith ".html" ⇒
        MergeStrategy.first
      case "application.conf" ⇒
        MergeStrategy.concat
      case PathList("org", "xmlpull", "v1", xs @ _ *) ⇒
        MergeStrategy.first
      case x ⇒
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(
      prependShellScript = Some(defaultShellScript)),
    assemblyJarName in assembly := s"${name.value}-${version.value}",
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value
  )
  .enablePlugins(SbtWeb, SbtTwirl)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .settings(
    name := "template-matching-client",
    persistLauncher := true,
    persistLauncher in Test := false,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= {
      Seq(
        "com.thoughtworks.binding" %%% "dom" % "10.0.1"
      )
    }
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
