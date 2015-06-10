import de.johoop.cpd4sbt.CopyPasteDetector._
seq(cpdSettings : _*)

lazy val commonSettings = Seq(
  organization := "io.ntdata",
  name := "Reactive Twitter",
  version := "0.0.1",
  scalaVersion := "2.11.6",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),

  libraryDependencies ++= {
    val akkaV = "2.3.9"
    Seq(
      "com.typesafe.akka"% "akka-actor_2.11" % akkaV,
      "org.clapper" %% "argot" % "1.0.3",
      "com.typesafe.akka" %  "akka-testkit_2.11"  % akkaV % "test",
      "org.specs2"          %%  "specs2-core"   % "3.5" % "test",
      "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
      "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-RC3",
      "org.twitter4j" % "twitter4j-stream" % "4.0.3"
    )
  }
)
lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime"
lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(commonSettings: _*).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "io.ntdata",
    buildInfoOptions += BuildInfoOption.ToMap
  )

