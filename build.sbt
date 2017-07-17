name := "extractor"
organization := "org.bzz"
version := "0.0.1"

scalaVersion := "2.11.8"
val sparkVersion = "2.1.0"

mainClass in Compile := Some("org.bzz.extractor.Driver")

//PB.targets in Compile := Seq(
//  scalapb.gen(flatPackage=true) -> file("src/main/scala")
//)

//dependencyOverrides += "io.netty" % "netty-all" % "4.1.12.Final"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",

  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % com.trueaccord.scalapb.compiler.Version.grpcJavaVersion
)