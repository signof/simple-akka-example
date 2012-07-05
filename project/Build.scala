import sbt._
import Keys._ 


object ExampleBuild extends AkkaBuild {
  def akkaModules = List("actor","remote")
  
  def computeSettings() = { 
    Defaults.defaultSettings ++ super.settings
  }
    
  lazy val project = Project(id = "akka-example",
                             base = file("."),
			     settings = computeSettings) 
}
