ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val ciOptionsFilter =
  if (!sys.env.contains("CI")) (options: Seq[String]) => options.filterNot(Set("-Xfatal-warnings")) else (options: Seq[String]) => options

val compilerOptions = scalacOptions ~= ciOptionsFilter.andThen(_ :+ "-Ymacro-annotations")

lazy val root = (project in file("."))
  .settings(
    name := "nafarmie",
    compilerOptions,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    libraryDependencies ++= Dependencies.backendDependencies
  )
