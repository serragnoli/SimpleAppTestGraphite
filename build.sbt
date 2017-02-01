name := "SimpleApp"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.4.0",
  "io.kamon" %% "kamon-spray" % "0.4.0",
  "io.kamon" %% "kamon-statsd" % "0.4.0",
  "io.kamon" %% "kamon-system-metrics" % "0.4.0"
)
