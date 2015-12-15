package org.blockfreie.helium.feuerkrake{
  import  java.lang.reflect.Method;
  import  org.blockfreie.helium.feuerkrake.annotation._;
  import java.lang.reflect.Method;
  import java.util.logging.Logger
  import java.util.logging.Level

  object Symbols {
    val THIS      : RowID = new RowID("this");
    val RESULT    : RowID = new RowID("result");
    val EXCEPTION : RowID = new RowID("exception");
  }

  object MethodUtilies {
    def nameFromMethod(method : Method) : String = method.getAnnotation(classOf[Table]).value
    def rowIDFromMethod(method : Method) : List[RowID]=method.getParameterAnnotations.map(annotations => annotations.find(annotation => annotation.annotationType().equals(classOf[Column]))).map(option => new RowID(option.get.asInstanceOf[Column].value)).toList
  }
  
  class MethodInfo(val rowID:List[RowID],val tableID:TableID){
    def this(method : Method) = this(MethodUtilies.rowIDFromMethod(method),new TableID(MethodUtilies.nameFromMethod(method)))
  }

  object AspectContext {
  import scala.util.parsing.combinator._
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  import java.util.logging.Logger
  import java.util.logging.Level

  val LOGGER : Logger = LogUtil.createLogger(Level.SEVERE)
  var catalog : Map[String,MethodInfo]  = Map[String,MethodInfo]()
  def register(method : Method) : Unit = {
      val tableName : String = MethodUtilies.nameFromMethod(method)
      catalog= catalog.updated(tableName,new MethodInfo(method))
      val tableID = new TableID(tableName)
      LOGGER.log(Level.INFO,"XXX:loading method %s".format(method.toString))
      Schema.table(tableID,methodToHandler(tableName,method))
  }
  def register[T](clazz  : Class[T]) : Unit = {clazz.getDeclaredMethods.filter((m : Method) => m.getAnnotation(classOf[Table]) != null ).map((m : Method) => register(m) ) }
    def handle(name : String,thiz : Any,args : List[Any],handler : List[Any] => Any) : Any = {
      val  methodInfo : MethodInfo = catalog(name)
      val  row        : Row = addThisToRow(parameterToRow(name,args),thiz)
      val  method     : Row => Row = ((x : Row) => { try { x.set(Symbols.RESULT,handler(rowToParametername(name,x))) }
						     catch { case e: Exception => x.set(Symbols.EXCEPTION,e) }
						   })
      LOGGER.log(Level.INFO,row.data.toString)
      Schema.row(methodInfo.tableID,method,row) match
      { case None => null
        case Some(row) => resultFromRow(row) 
      }
      
    }
    def methodToHandler(name : String,method : Method) = (row : Row) => {
      val values   : List[Object] = rowToParametername(name,row).map(x => x.asInstanceOf[Object])
      val objekt   : Any       = if(row.data.contains(Symbols.THIS)){ row.data(Symbols.THIS)} else { null }
      val result   : Any       = method.invoke(objekt,values.toArray:_*)
      Console.println(values);
      result
    } : Any
    def parameterToRow(name : String,args : List[Any]) : Row = {
      val methodInfo    : MethodInfo  = catalog(name)
      val rowIDList     : List[RowID] = methodInfo.rowID 
      val labeledArgs   : List[(RowID,Any)] = rowIDList.zip(args)
      val succArgsToRow : ((Row, (RowID, Any)) => Row) = ((row : Row ,arg : (RowID,Any))  => { row.set(arg._1,arg._2) } : Row)
      val row           : Row = labeledArgs.foldLeft (new Row()) (succArgsToRow)
      row
    }
    def rowToParametername(name : String,row : Row) : List[Any] = {
      val methodInfo    : MethodInfo        = catalog(name)
      val rowIDList     : List[RowID]       = methodInfo.rowID 
      val args          : List[Any]         = methodInfo.rowID.map( rowID => row.data(rowID))
      args
    }
    def resultFromRow(row : Row) : Any = row.data(Symbols.RESULT)
    def addThisToRow(row: Row,thiz : Any) = row.set(Symbols.THIS,thiz)
    def thisFromRow(row : Row) : Any = row.data(Symbols.THIS)
  }
}
/*
      val name          : String            = "";


      val labeledArgs   : List[(RowID,Any)] = rowIDList.zip(args)
      val succArgsToRow : ((Row, (RowID, Any)) => Row) = ((row : Row ,arg : (RowID,Any))  => { val (rowID : RowID,value : Any) = arg  ; new Row(row.data.updated(rowID,value)) } : Row)
      val row           : Row = labeledArgs.foldLeft (List[Any]()) (succArgsToRow)


class Row(val data : Map[RowID,Any]);

     tableFromMethod       : Method -> String
     rowIDFromMethod       : Method -> List[RowID]
     rowHandlerFromMethod  : Method -> (Row -> Unit)

     registerMethod : Method -> Unit
      table
      save information

     registerClass  : Class  -> Unit

     jParameter : Object * List [Object]
     jFunction  : jParameter -> Object

     handle           : String * jParameter * jMethod -> Object
     functionToMethod : String * jMethod    -> (Row -> Row)
     getResultFromRow : Row -> Object

class TestA {
  @Table("tableA") def tableA(@Column(value="a") a : Int,@Column(value="b") b : Int) = ""
}
class TestB {
  @Table("tableA") def tableA(@Column(value="a") a : Int,@Column(value="b") b : Int) = ""
}


class TestA {
 def tableA(a : Int,b : Int) = ""
}
import java.lang.reflect.Method
val values   : List[Object] = List[Object](1.asInstanceOf[Object],2.asInstanceOf[Object])
val method : Method = classOf[TestA].getMethods.head
method.invoke(new TestA(),values.toArray:_*)
* 

def 

Class c = Class.forName(a);
*/
