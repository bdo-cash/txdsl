// format: off
name := baseDirectory.value.getName
organization := "hobby.chenai.nakam"
version := "1.1.0"

scalaVersion := "2.12.18"
crossScalaVersions := Seq(
  "2.11.12",
  "2.12.18",
  "2.13.10"
)

lazy val scalaSettings = Seq(
  scalaVersion := "2.12.18"
)

//lazy val root = Project(id = "txdsl", base = file("."))
//  .dependsOn(/*lang*/)
//  .settings(scalaSettings,
//    aggregate in update := false
//  )

// 启用对 java8 lambda 语法的支持。
//scalacOptions += "-Xexperimental"

exportJars := true

offline := true

// 解决生成文档报错导致 jitpack.io 出错的问题。
packageDoc / publishArtifact := false

// 如果要用 jitpack 打包的话就加上，打完了再注掉。
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.github.bdo-cash" % "scala-lang" % "60a29ca4c7", // scala 2.12
  "com.github.bdo-cash" % "reflow" % "aa2e5b7908",

  "junit" % "junit" % "[4.12,)" % Test,
  "org.scalatest" %% "scalatest" % "3.2.0-SNAP7" % Test
)
