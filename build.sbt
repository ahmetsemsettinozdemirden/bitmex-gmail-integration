name := """bitmex-api"""
organization := "com.operationdollar"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

resolvers += Resolver.sbtPluginRepo("releases")

libraryDependencies += guice

libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += "com.auth0" % "java-jwt" % "3.3.0"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"