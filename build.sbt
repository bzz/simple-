name := "berserker"
organization := "tech.sourced"
version := "0.0.1"

scalaVersion := "2.11.8"
val sparkVersion = "2.1.0"

mainClass in Compile := Some("com.srcd.berserker.BerserkerDriver")

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots",
  "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
)

// Force to use this netty dependency due conflicts
dependencyOverrides += "io.netty" % "netty-all" % "4.1.12.Final"

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.11")
libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test", 

  "io.grpc" % "grpc-netty" % com.trueaccord.scalapb.compiler.Version.grpcJavaVersion,
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion,
)
