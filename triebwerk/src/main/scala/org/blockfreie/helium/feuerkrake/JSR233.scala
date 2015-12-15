import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory
import javax.script.ScriptEngineFactory

package org.blockfreie.helium.feuerkrake{
  //Implement javax.script.ScriptEngine.
  class AQLScriptEngine extends javax.script.ScriptEngine {
    override def eval(script : String,context : javax.script.ScriptContext) : Object = null
    override def eval(reader : java.io.Reader,bindings : javax.script.Bindings) : Object = null
    override def eval(script : String) : Object = null
    override def eval(reader : java.io.Reader) : Object = null
    override def eval(script : String, bindings : javax.script.Bindings) : Object = null
    override def eval(reader : java.io.Reader,bindings: javax.script.ScriptContext) : java.lang.Object = null
    override def put(key : String,value : Object) : Unit = {}
    override def get(key : String) : Object = null
    override def getBindings(scope : Int) : javax.script.Bindings = null
    override def setBindings(bindings : javax.script.Bindings,scope : Int) : Unit = {}
    override def createBindings() : javax.script.Bindings = null
    override def getContext() : javax.script.ScriptContext = null
    override def setContext(context : javax.script.ScriptContext) : Unit = {}
    override def getFactory() : javax.script.ScriptEngineFactory = null
  }
  //Implement javax.script.ScriptEngineFactory.
  class AQLScriptingFactory extends javax.script.ScriptEngineFactory {
    override def getEngineName() : String = null
    override def getEngineVersion() : String = null
    override def getExtensions() : java.util.List[String]= null
    override def getMimeTypes() : java.util.List[String]= null
    override def getNames() : java.util.List[String]= null
    override def getLanguageName() : String = null
    override def getLanguageVersion() : String = null
    override def getParameter(key : String) : Object = null
    override def getMethodCallSyntax(obj : String,m : String,args : String*) : String = null
    override def getOutputStatement(toDisplay : String) : String = null
    override def getProgram(statements : String*) : String = null
    override def getScriptEngine() : javax.script.ScriptEngine = null
  }

  class AQLScriptEngineFactory extends javax.script.ScriptEngineFactory {
    override def getEngineName() : String = null
    override def getEngineVersion() : String = null
    override def getExtensions() : java.util.List[String] = null
    override def getMimeTypes() : java.util.List[String] = null
    override def getNames() : java.util.List[String] = null
    override def getLanguageName() : String = null
    override def getLanguageVersion() : String = null
    override def getParameter(key : String) : Object = null
    override def getMethodCallSyntax(obj : String,m : String,args : String*) : String = null
    override def getOutputStatement(toDisplay : String) : String = null
    override def getProgram(statements : String*) : String = null
    override def getScriptEngine() : javax.script.ScriptEngine = null
  }
}
