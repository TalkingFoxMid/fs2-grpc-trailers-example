import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val client = (project in file("client"))
  .settings(
    name := "grpcO-client",
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-stub" % scalapb.compiler.Version.grpcJavaVersion
  ).dependsOn(proto).aggregate(proto)

lazy val server = (project in file("server"))
  .settings(
    name := "grpcO-server",
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-stub" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-core" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies +=     "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    libraryDependencies +=     "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    libraryDependencies += "io.grpc" % "grpc-services" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-protobuf" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.22.3"


  ).dependsOn(proto).aggregate(proto)


lazy val proto = (project in file("proto"))
  .settings(
    name := "grpcO-proto",

    libraryDependencies +=     "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
//      Compile / PB.targets := Seq(
//      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
//    ),
    libraryDependencies += "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-stub" % scalapb.compiler.Version.grpcJavaVersion,
    libraryDependencies += "io.grpc" % "grpc-core" % scalapb.compiler.Version.grpcJavaVersion,
      libraryDependencies +=     "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,



  ).enablePlugins(Fs2Grpc)
