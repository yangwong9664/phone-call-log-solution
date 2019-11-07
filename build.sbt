lazy val phoneCompany = (project in file(".")).settings(
  Seq(
    name := "disco-test-phone-company",
    version := "1.0",
    scalaVersion := "2.12.3"
  )
)
scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test"
resourceDirectory in Compile := baseDirectory.value / "conf"

parallelExecution in Test := false
fork in Test := true

val akkaVersion     = "2.5.23"
val akkaHttpVersion = "10.1.5"

libraryDependencies ++= {
  Seq(
    "com.google.inject" % "guice" % "4.2.2",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
    "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "1.1.2"
  )
}
