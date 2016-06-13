lazy val root = Project("root", file("."))
  .aggregate(basics)
  .settings(BaseSettings.settings: _*)

lazy val basics = Project("basics", file("basics"))
  .settings(BaseSettings.settings: _*)
  .settings(Dependencies.basics: _*)