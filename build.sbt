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
libraryDependencies += "com.google.apis" % "google-api-services-gmail" % "v1-rev20190120-1.28.0"
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.28.0"
libraryDependencies += "com.google.oauth-client" % "google-oauth-client-jetty" % "1.28.0"
libraryDependencies += "org.quartz-scheduler" % "quartz" % "2.3.0"