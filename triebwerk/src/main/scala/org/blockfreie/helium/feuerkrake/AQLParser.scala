package org.blockfreie.helium.feuerkrake{
  import scala.util.parsing.combinator._
  import org.blockfreie.element.hydrogen.murikate.LogUtil
 object AQL { 
    type MethodRef    = (Any*) => Any
    type Continuation = (Any => Unit)
    type Handler      = (Context,Continuation) => Unit
    class Context(val vTable : Map[String,MethodRef],val row : Row)
    { def this()= this(Map[String,MethodRef](),new Row()) }
    
    object Handler {
      import java.util.logging.Logger
      import java.util.logging.Level

      val LOGGER : Logger = LogUtil.createLogger(Level.INFO)
      def insertStatement    (name : String, label : List[String] , expression : List[Context => Any])  = ((context : Context,continuation : Continuation) => {
	val map : Map[String,Context => Any] = label.zip(expression).foldLeft(Map[String,Context => Any]())((acc,value) => acc.updated(value._1,value._2))
	val row : Row = map.foldLeft (new Row()) ((acc,value) => acc.set(new RowID(value._1),value._2(context)))
        val tableID : TableID = new TableID(name)
	Schema.insert(tableID,row,x => continuation(x))
      }) : Handler
      def callStatement   (expression : Context => Any) = ((context : Context,continuation : Continuation) => { continuation(expression(context)) }) : Handler
      def selectStatement    (projectionOption : Option[List[Context => Any]],id : String,expression : Option[Context => Any]) = ((context : Context,continuation : Continuation) => {
      val tableID     : TableID = new TableID(id)
      val handler     : Row => Unit = ((row : Row) => {
	val merged              : Row = row.merge(context.row)
	val localContext        : Context   = new Context(context.vTable,merged)
	val doSelect            : Boolean = expression match { case None => true
                                                               case Some(bExpression) => bExpression(localContext).asInstanceOf[Boolean] }
	if(doSelect){
	  val data   : Map[String,Any]  =	projectionOption match {
	    case None => { merged.data.foldLeft(Map[String,Any]())((acc,value)=> acc + (value._1.id -> value._2)) }
	    case Some(projections) => {
	      val columnValues : List[Any] = projections.map(f => f(localContext))
	      var index = 0L;
	      columnValues.foldLeft(Map[String,Any]())((acc,value)=> acc + (({index =index+1;index}).toString -> value)) 
	    }
	  }
	continuation(data);
      } else 
      {()}
     })
     Schema.select(tableID,handler)
      }) : Handler
      def updateStatement    (id : String, map : Map[String,Context => Any],expression : Option[Context => Any]) = ((context : Context,continuation : Continuation) => {
	val tableID     : TableID = new TableID(id)
	def contextToRow(f : Context => Any,rowID : RowID) = (row : Row ) => { val merged              : Row     = row.merge(context.row)
									       val localContext        : Context = new Context(context.vTable,merged)
								               val doUpdate            : Boolean = expression match{
									                                    case None       => true
                                                                                                            case Some(expr) => expr(localContext).asInstanceOf[Boolean]
									                                 }
                                                                               if(doUpdate){ f(localContext) } else { row.data(rowID) }
									     }
//	Console.println(map)
	val update :  Map[RowID,(Row => Any)]  = map.foldLeft( Map[RowID,(Row => Any)]())((acc,value)=> { val rowID : RowID = new RowID(value._1); acc.updated(rowID,contextToRow(value._2,rowID)) })
	Schema.update(tableID,update)
      }) : Handler
      def deleteStatement    (id : String, expression : Option[Context => Any]) = ((context : Context,continuation : Continuation) => {
	val tableID     : TableID = new TableID(id)
	val filter      = (row : Row ) => { val merged         : Row     = row.merge(context.row)
                                            val localContext   : Context = new Context(context.vTable,merged)
					    expression match{ case None       => true
                                                              case Some(expr) => expr(localContext).asInstanceOf[Boolean]
							    }
					  }
	Schema.delete(tableID,filter)
      }) : Handler
    }
   
    object Parser extends RegexParsers with RunParser {
      
      val ID                          = """([a-zA-Z]+[a-zA-Z0-9]*)"""r
      def id : Parser[String]         = ID ^^ (_.mkString)
    
      val STRING                      = ("\""+"""([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*"""+"\"").r    
      def string : Parser[String]     = STRING ^^ { case m => m.substring(1,m.length-1) }
    						
      val WHOLENUMBER                 =  """(-|\+)?\d+"""
      def wholeNumber : Parser[Long]  = WHOLENUMBER ^^ {case m => java.lang.Long.parseLong(m) }
      
      val NUMBER                      = """(-|\+)?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?[fFdD]?""".r
      def number                      = NUMBER ^^  { case m => if(m.matches(WHOLENUMBER)){ java.lang.Long.parseLong(m).asInstanceOf[Any] } else { java.lang.Double.parseDouble(m).asInstanceOf[Any] }}
          
      val NULL                        = "(?i)null"r
      val TRUE                        = "(?i)true"r
      val FALSE                       = "(?i)false"r
      
      def value: Parser[Context => Any] = (
	  number                 ^^ (x => (c : Context) => x)
	| string                 ^^ (x => (c : Context) => x) 
	| NULL                   ^^ (x => (c : Context) => null)
	| TRUE                   ^^ (x => (c : Context) => true)
	| FALSE                  ^^ (x => (c : Context) => false)
	| id                     ^^ (x => (c : Context) => c.row.data(new RowID(x)))
    )
    
    val LPAREN   = "("
    val COMMA    = ","
    val RPAREN   = ")"
    val EQUAL    = "="r
    val OR       = "(?i)or"r
    val AND      = "(?i)and"r

    def functionExpression: Parser[Context => Any] = ( id ~ LPAREN ~ repsep(functionExpression,COMMA) ~ RPAREN  ^^ { case (id : String) ~ _ ~ args ~ _  => ((c : Context) => { val methodRef : MethodRef = c.vTable(id);
    val argMethod : List[Context => Any] = args.map(x=>x.asInstanceOf[Context => Any]);
    val argValue  : List[Any] = args.map(x => x.apply(c));
    methodRef(argValue:_*) })}
    | LPAREN ~> expression  <~ RPAREN ^^ (f => (c : Context) => f(c))
    | value ^^ (f => (c : Context) => f(c)) )
      def equalExpression                            = (functionExpression ~  EQUAL ~ functionExpression ^^ { case lhs ~ _ ~rhs =>  ((c :Context) => {val lValue : Any = lhs.apply(c)                                                                                                
    val rValue : Any = rhs.apply(c)                                                                                          
    ((lValue == null) && (rValue == null)) || ((lValue != null) && (lValue.equals(rValue)))                                   
                                                                                                                                                    })   }
    | functionExpression ^^ (f => (c : Context) => f(c)))
    def andExpression : Parser[Context => Any] = (equalExpression ~ (( AND ~> equalExpression)*) ^^ { case head ~ tail => if(tail.length == 0) ((c : Context) => head(c)) else ((c : Context) => tail.foldLeft(head(c).asInstanceOf[Boolean])((acc,value) => acc && value(c).asInstanceOf[Boolean] )   ) })
    def orExpression : Parser[Context => Any] = (andExpression ~ (( OR ~> andExpression)*) ^^ { case head ~ tail => if(tail.length == 0) ((c : Context) => head(c)) else ((c : Context) => tail.foldLeft(head(c).asInstanceOf[Boolean])((acc,value) => acc || value(c).asInstanceOf[Boolean] )   ) })
    def expression : Parser[Context => Any] = orExpression
    def tupleExpression   : Parser[List[Context => Any]] = expression ~ (( COMMA ~> expression)*) ^^ { case head ~ tail => tail.foldLeft(List(head))((acc,value) => acc ++ List(value)) }
    def tupleLabel        : Parser[List[String]]         = id ~  (( COMMA ~> id)*) ^^ { case head ~ tail => tail.foldLeft(List[String](head.asInstanceOf[String]))((acc,value) => acc ++ List(value.asInstanceOf[String])) }
    
    val INSERT    = "(?i)insert"r
    val INTO      = "(?i)into"r
    val VALUES    = "(?i)values"r

    def insertStatement   : Parser[(Context,Continuation) => Unit] = INSERT ~ INTO ~ id  ~ LPAREN ~ tupleLabel ~ RPAREN ~ VALUES ~ LPAREN ~ tupleExpression ~ RPAREN ^^ { case _ ~ _ ~ id ~ _ ~ tupleLabel ~ _ ~ _ ~ _ ~ tupleExpression ~ _ => { val _id : String = id ; val _tupleLabel : List[String] = tupleLabel; val _tupleExpression : List[Context => Any] = tupleExpression; Handler.insertStatement(_id,_tupleLabel,_tupleExpression) } }

    
    val SELECT    = "(?i)select"r
    val STAR      = "*"
    val FROM      = "(?i)from"r
    val WHERE     = "(?i)where"r
   
    def projectStatement   : Parser[Option[List[Context => Any]]]  = ( STAR ^^ { _ => None } | tupleExpression ^^ { e => Some(e) })
    def selectStatement    : Parser[Handler] = SELECT ~ projectStatement ~ FROM ~ id ~ ((WHERE ~> expression)?) ^^ { case _ ~ projectStatement ~ _ ~ id ~ expression => { val _projectStatement : Option[List[Context => Any]] =  projectStatement; val _id : String = id; val _expression : Option[Context => Any] = expression; Handler.selectStatement(_projectStatement,_id,_expression) } }

   val DELETE    = "(?i)delete"r
   def deleteStatement    : Parser[Handler] = DELETE ~ FROM ~ id  ~ (( WHERE ~> expression)?) ^^ { case _ ~ _ ~ id ~ expression => { val _id : String = id; val _expression : Option[Context => Any] = expression; Handler.deleteStatement(_id,_expression) }}
   
   val UPDATE    = "(?i)update"r 
   val SET       = "(?i)set"r 
   def updateSetExpression : Parser[Map[String,Context => Any]] = id ~ EQUAL ~ expression ^^ { case id ~ _ ~ expression => Map[String,Context => Any](id -> expression) }
   def updateSet  : Parser[Map[String,Context => Any]] = updateSetExpression ~ ((COMMA ~> updateSetExpression)*) ^^ { case head ~ tail => val _head : Map[String,Context => Any] = head ; val _tail : List[Map[String,Context => Any]] = tail; tail.foldLeft(head)((acc,value) => acc ++ value) }
   def updateStatement : Parser[Handler] = UPDATE  ~ id ~ SET ~ updateSet ~ (( WHERE ~> expression)?) ^^ { case _ ~ id ~ _ ~ map ~ exp =>  val _id : String = id ; val _map : Map[String,Context => Any] = map; val _exp : Option[Context => Any] = exp; Handler.updateStatement(_id,_map,_exp)  }

   val CALL   = "(?i)call"r
   def callStatement : Parser[Handler] = CALL ~ expression ^^ { case _ ~ exp => val _exp : (Context => Any) = exp; Handler.callStatement(_exp) }
   
   def statement : Parser[Handler] = (insertStatement | deleteStatement | selectStatement | updateStatement | callStatement) ^^ { case s => s }
   
   type RootType = Handler
   def root = statement
    }
  }
}

/*
  import  org.blockfreie.helium.feuerkrake.AQL._
  import org.blockfreie.helium.feuerkrake._

  import org.junit.AfterClass;
  import org.junit.BeforeClass;
  import org.junit.Test;
  import junit.framework.Assert;

  import java.util.logging.Logger
  import java.util.logging.Level

  import org.blockfreie.element.hydrogen.murikate.LogUtil

  val LOGGER : Logger = LogUtil.createLogger(Level.INFO)

  val row = new Row(Map[RowID,Any](new RowID("ab") -> 1L))
  val one  : MethodRef = (_ => 1L) 
  val sum  : MethodRef = (x => x.map(y => (y.asInstanceOf[Long] : Long)).foldLeft(0L)(_ + _))
  val odd  : MethodRef = (x => x.map(y => (y.asInstanceOf[Long] : Long)).foldLeft(0L)(_ + _) % 2L == 1 )
  val context = new Context(Map[String,MethodRef]("one" -> one , "sum" -> sum , "odd" -> odd),row)

  AspectContext.register(classOf[org.blockfreie.helium.feuerkrake.test.TestA])
    

  val random : scala.util.Random = new scala.util.Random
  def randomTuple : (Long,Long) = (scala.math.abs(random.nextLong),scala.math.abs(random.nextLong))
  val addTableName : String = org.blockfreie.helium.feuerkrake.test.TestA.ADD_TABLE_NAME

  def statement(in : String)= AQL.Parser.run(in)  
  
  val selectTemplateSQL  : String = "select a,b,result from %s"
  val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
  val selectParsedSQL    = statement(selectFormattedSQL)
  var actual             : Map[String,Any] = Map[String,Any]()
  val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
  selectParsedSQL.get.apply(context,continuation)
  
  val deleteTemplateSQL  : String = "delete from %s where odd(a,b)"
  val deleteFormattedSQL : String = deleteTemplateSQL.format(addTableName)  
  val deleteParsedSQL    = statement(deleteFormattedSQL)
  deleteParsedSQL.get.apply(context,continuation)

  val (aValue,bValue) : (Long,Long)   = randomTuple
  val expected           : Map[String,Any] = Map[String,Any]("1" -> aValue , "2" ->bValue,"3" -> (aValue+bValue))
  val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
  AQL.Parser.run(insertSQL).get.apply(context,_ => ())
  if(((aValue + bValue) % 2L) == 1)
  { Assert.assertEquals(Map[String,Any](),actual) } else { Assert.assertEquals(expected,actual) }

    Assert.assertTrue(actual("1").asInstanceOf[Long] + actual("2").asInstanceOf[Long]  == actual("3").asInstanceOf[Long]) }
   ((((aValue + bValue) % 2L) == 1),actual("3"))


  if(((aValue + bValue) % 2L) == 1) { Assert.assertTrue(actual("1").asInstanceOf[Long] + actual("2").asInstanceOf[Long]  == actual("3").asInstanceOf[Long]) }
  (actual,(((aValue + bValue) % 2L) == 1))
  Assert.assertTrue(((((aValue + bValue) % 2L) == 1) == false) == (actual.size == 0))  
  }

  val selectTemplateSQL  : List[String] = List("select *   from %s","select a,b from %s","select a,b from %s where a = 1","select a,b from %s where a = 1 and b = 2")
  val selectFormattedSQL : List[String] = selectTemplateSQL.map(x => x.format(addTableName))
  val selectParsedSQL    = selectFormattedSQL.map(x => selectStatement(x))
  val continuation : AQL.Continuation = (x => () )
  selectParsedSQL.map(x => x.get.apply(context,continuation) )
  
  
  val selectSQL : String = "select * from %s".format(addTableName)

  val (aValue,bValue) : (Long,Long)   = randomTuple
  val insertSQL : String = "insert %s into (a,b) values (:%d,%d)".format(addTableName,aValue,bValue)
  var actual    : Map[String,Long] = Map[String,Long]()
  val expected  : Map[String,Long] = Map[String,Long]("a" -> aValue,"b" -> bValue)
  val continuation : AQL.Continuation = (x => {LOGGER.log(Level.INFO,x.toString);actual = x.asInstanceOf[Map[String,Long]]})
  AQL.Parser.run(selectSQL)
  def selectStatement(in : String)= AQL.Parser.run(AQL.Parser.selectStatement,in)

  BoboParser.run(selectSQL).get.apply((x => {LOGGER.log(Level.INFO,x.toString);actual = x.asInstanceOf[Map[String,Long]]}))
  BoboParser.run(insertSQL).get.apply((x => () ))
      Assert.assertTrue(expected.forall(x => {actual(x._1) == x._2}))

  Schema.catalog(new TableID("a965c30f2190511e0ac640800200c9a")).update
*/
