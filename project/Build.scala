import sbt._
import Keys._ 


object ExampleBuild extends AkkaBuild {
  def akkaModules = List("actor","remote")
  
  def computeSettings() = { 
    Defaults.defaultSettings ++ super.settings ++
    Seq[Setting[_]](
      resolvers ++= Seq(               
        "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
      ),
      libraryDependencies ++= Seq( 
        "com.romix.akka" % "akka-kryo-serialization" % "0.1-SNAPSHOT"
      ))
  }
    
  lazy val project = Project(id = "akka-example",
                             base = file("."),
			     settings = computeSettings) 
}
