name := "SimpleApp"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Boundless Repository" at "http://repo.boundlessgeo.com/main/"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-spray" % "0.4.0",
  "io.dropwizard.metrics" % "metrics-core" % "3.1.2",
  "io.dropwizard.metrics" % "metrics-graphite" % "3.1.2",
  "io.dropwizard.metrics" % "metrics-jvm" % "3.1.2"
)
