scalaVersion := "2.11.8"
organization := "practechal.me"
name := "template-matching"

libraryDependencies ++= {
  val boofcvVersion = "0.24.1"
  val boofcvOrg = "org.boofcv"
  Seq(
    boofcvOrg % "core" % boofcvVersion
  )
}
