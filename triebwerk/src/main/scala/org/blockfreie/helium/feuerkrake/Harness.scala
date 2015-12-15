import org.blockfreie.helium.feuerkrake._
package org.blockfreie.helium.feuerkrake{
  import scala.util.parsing.combinator._
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  import  org.blockfreie.helium.feuerkrake.annotation._;
  import org.blockfreie.helium.feuerkrake.client.IHarness
  import org.blockfreie.helium.feuerkrake.AQL._
  import java.lang.reflect._
  import java.util.logging.Logger
  import java.util.logging.Level
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  
  class Harness extends IHarness {
    val LOGGER : Logger = LogUtil.createLogger(Level.ALL)
    var lock : AnyRef = new Object()
    var context : AQL.Context = new AQL.Context()
    var messages : List[Any] = List[Any]();

    def init(methods : String,tables : String) = { setMethods(methods); setTables(tables) }

    def setMethods(clazzName : String) : Unit = { registerMethods(clazzName) }
    def registerMethods(clazzName : String): Unit = clazzName.split(",").map((x : String) => registerMethods(Class.forName(x)))
    def registerMethods[T](clazz  : Class[T]) : Unit = {clazz.getDeclaredMethods.filter((m : Method) => m.getAnnotation(classOf[Export]) != null ).map((m : Method) => registerMethods(m) ) }
    def registerMethods(method : Method) : Unit = {
      def nameFromMethod(method : Method) : String = method.getAnnotation(classOf[Export]).value
      val tableName : String = nameFromMethod(method)
      val methodRef : AQL.MethodRef = (args => method.invoke(null,args.asInstanceOf[Seq[java.lang.Object]]:_*))
      LOGGER.log(Level.INFO,"harness : loading method %s as table '%s'".format(method,tableName))
      context = new AQL.Context(context.vTable.updated(tableName,methodRef),context.row)
    }

    def setTables(clazzName : String) : Unit = registerTables(clazzName)
    def registerTables(clazzName : String): Unit = clazzName.split(",").map((x : String) => registerTables(Class.forName(x)))
    def registerTables[T](clazz : Class[T]) : Unit = { 
      LOGGER.log(Level.INFO,"harness : loading tables from class '%s'".format(clazz));
      AspectContext.register(clazz)
    }
    def getMessages() : java.util.List[Object] = lock.synchronized { 
      val result = new java.util.ArrayList[Object]();
      messages.foreach( m => result.add(m.asInstanceOf[Object]));
      messages = List[Any]();
      result
    } : java.util.ArrayList[Object]

    val continuation       : AQL.Continuation = lock.synchronized { x => messages=x::messages }
    val sqlContinuation    : AQL.Continuation = { x  => { val jmap = new java.util.HashMap[String,Any](); x.asInstanceOf[Map[String,Any]].foreach(t => { val (key,value) = t; jmap.put(key,value)  }) ; messages=jmap::messages } }
    def query(sql : String)   = AQL.Parser.run(sql).get.apply(context,sqlContinuation)
    def execute(sql : String) = AQL.Parser.run(sql).get.apply(context,continuation)
    def handle(message : Any)  = lock.synchronized { messages = message.asInstanceOf[String] :: messages } : Unit
  }
}
