import sbt._ 
import Keys._
// my poor man's akka plugin replacement. (doesn't seem to work with sbt-0.11.0)

trait AkkaBuild extends Build { 
  val akkaVersion = "2.0"
  val akkaModulesVersion = "2.0"


  lazy val akkaRepoList = Seq(
	    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  )

   def akkaModule(module: String) = "com.typesafe.akka" % ("akka-" + module) % {
     if (Set("scalaz", "camel", "dispatcher-extras", "modules-samples", "kernel", "dist", "spring", "camel-typed", "amqp").contains(module))
       akkaModulesVersion
     else
       akkaVersion
   }
  

  def akkaModules : Seq[String]

  override def settings = super.settings ++ Seq(
    libraryDependencies ++= (akkaModules map akkaModule) , 
    resolvers ++= akkaRepoList
  )

}


