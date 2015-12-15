package org.blockfreie.helium.feuerkrake{
  import org.junit.After;
  import org.junit.Before;
  import org.junit.Test;
  import junit.framework.Assert;
  import org.blockfreie.helium.feuerkrake.AQL._
  class TestAQLParser{
    val row = new Row(Map[RowID,Any](new RowID("ab") -> 1L))
    val one  : MethodRef = (_ => 1L) 
    val sum  : MethodRef = (x => x.map(y => (y.asInstanceOf[Long] : Long)).foldLeft(0L)(_ + _))
    val odd  : MethodRef = (x => x.map(y => (y.asInstanceOf[Long] : Long)).foldLeft(0L)(_ + _) % 2L == 1 )
    val context = new Context(Map[String,MethodRef]("one" -> one , "sum" -> sum , "odd" -> odd),row)

    val random : scala.util.Random = new scala.util.Random
    def randomTuple : (Long,Long) = (scala.math.abs(random.nextLong),scala.math.abs(random.nextLong))
    val addTableName : String = org.blockfreie.helium.feuerkrake.test.TestA.ADD_TABLE_NAME
    def statement(in : String)= AQL.Parser.run(in)
    @Test def updateCanFilterUpdate{
  val selectTemplateSQL  : String = "select a,b,result from %s"
  val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
  val selectParsedSQL    = statement(selectFormattedSQL)
  var actual             : Map[String,Any] = Map[String,Any]()
  val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
  selectParsedSQL.get.apply(context,continuation)
  
  val updateTemplateSQL  : String = "update %s set a = 1 , b = 2 where odd(a,b)"
  val updateFormattedSQL : String = updateTemplateSQL.format(addTableName)  
  val updateParsedSQL    = statement(updateFormattedSQL)
  updateParsedSQL.get.apply(context,continuation)

  val (aValue,bValue) : (Long,Long)   = randomTuple
  val expected           : Map[String,Any] = Map[String,Any]("1" -> 1L , "2" -> 2L,"3" ->3L)
  val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
  AQL.Parser.run(insertSQL).get.apply(context,_ => ())
  if(((aValue + bValue) % 2L) == 1)
  { Assert.assertEquals(expected,actual) 
  } else { 
    Assert.assertTrue(actual("1").asInstanceOf[Long] + actual("2").asInstanceOf[Long]  == actual("3").asInstanceOf[Long]) }

    }
    @Test def deleteCanFilter{
      for (i <- 0 to 100) {
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

      }
    }
    @Test def deleteAll{
      for (i <- 0 to 100) {
  val selectTemplateSQL  : String = "select a,b,result from %s"
  val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
  val selectParsedSQL    = statement(selectFormattedSQL)
  var actual             : Map[String,Any] = Map[String,Any]()
  val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
  selectParsedSQL.get.apply(context,continuation)
  
  val deleteTemplateSQL  : String = "delete from %s"
  val deleteFormattedSQL : String = deleteTemplateSQL.format(addTableName)  
  val deleteParsedSQL    = statement(deleteFormattedSQL)
  deleteParsedSQL.get.apply(context,continuation)

  val (aValue,bValue) : (Long,Long)   = randomTuple
  val expected           : Map[String,Any] = Map[String,Any]("1" -> aValue , "2" ->bValue,"3" -> (aValue+bValue))
  val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
  AQL.Parser.run(insertSQL).get.apply(context,_ => ())
  Assert.assertEquals(Map[String,Any](),actual);
      }
    }

    @Test def updateCanUpdate{
      val selectTemplateSQL  : String = "select a,b,result from %s"
      val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
      val selectParsedSQL    = statement(selectFormattedSQL)
      var actual             : Map[String,Any] = Map[String,Any]()
      val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
      selectParsedSQL.get.apply(context,continuation)

      val updateTemplateSQL  : String = "update %s set a = 1 , b = 2"
      val updateFormattedSQL : String = updateTemplateSQL.format(addTableName)  
      val updateParsedSQL    = statement(updateFormattedSQL)
      updateParsedSQL.get.apply(context,continuation)

      val (aValue,bValue) : (Long,Long)   = randomTuple
      val expected           : Map[String,Any] = Map[String,Any]("1" -> 1L , "2" -> 2L,"3" ->3L)
      val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
      AQL.Parser.run(insertSQL).get.apply(context,_ => ())
      Assert.assertEquals(expected,actual)
    }
    @Test def testSelectFilterStatement(){
      def selectStatement(in : String)= AQL.Parser.run(in)  
      for (i <- 0 to 100) {
	val selectTemplateSQL  : String = "select a,b,result from %s where odd(a,b)"
	val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
	val selectParsedSQL    = selectStatement(selectFormattedSQL)
	var actual             : Map[String,Any] = Map[String,Any]()
	val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
	  selectParsedSQL.get.apply(context,continuation)
	val (aValue,bValue) : (Long,Long)   = randomTuple
	val expected           : Map[String,Any] = Map[String,Any]("a" -> aValue,"b" ->bValue)
	val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
	AQL.Parser.run(insertSQL).get.apply(context,_ => ())
	if(((aValue + bValue) % 2L) == 1) { Assert.assertTrue(actual("1").asInstanceOf[Long] + actual("2").asInstanceOf[Long]  == actual("3").asInstanceOf[Long]) }
	(actual,(((aValue + bValue) % 2L) == 1))
	Assert.assertTrue(((((aValue + bValue) % 2L) == 1) == false) == (actual.size == 0))  
      }
    }
    @Test def testSelectProjectStatement(){
      def selectStatement(in : String)= AQL.Parser.run(in)  
      val selectTemplateSQL  : String = "select a,b,result from %s"
      val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
      val selectParsedSQL    = selectStatement(selectFormattedSQL)
      var actual             : Map[String,Any] = Map[String,Any]()
      val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
      selectParsedSQL.get.apply(context,continuation)
      val (aValue,bValue) : (Long,Long)   = randomTuple
      val expected           : Map[String,Any] = Map[String,Any]("a" -> aValue,"b" ->bValue)
      val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
      AQL.Parser.run(insertSQL).get.apply(context,_ => ())
      Assert.assertTrue(actual("1").asInstanceOf[Long] + actual("2").asInstanceOf[Long]  == actual("3").asInstanceOf[Long])
    }
    @Test def testSelectStarStatement(){
      def selectStatement(in : String)= AQL.Parser.run(in)  
      val selectTemplateSQL  : String = "select *   from %s"
      val selectFormattedSQL : String = selectTemplateSQL.format(addTableName)  
      val selectParsedSQL    = selectStatement(selectFormattedSQL)
      var actual             : Map[String,Any] = Map[String,Any]()
      val continuation       : AQL.Continuation = (x => actual=x.asInstanceOf[Map[String,Any]] )
      selectParsedSQL.get.apply(context,continuation)
      val (aValue,bValue) : (Long,Long)   = randomTuple
      val expected           : Map[String,Any] = Map[String,Any]("a" -> aValue,"b" ->bValue)
      val insertSQL : String = "insert into %s (a,b) values (%d,%d)".format(addTableName,aValue,bValue)
      AQL.Parser.run(insertSQL).get.apply(context,_ => ())
      Assert.assertTrue(expected.forall(x => {actual(x._1) == x._2}))
    }
    @Test def testSelectStatement(){
      def selectStatement(in : String)= AQL.Parser.parseAll(AQL.Parser.selectStatement,in)
      val selectTemplateSQL  : List[String] = List("select *   from %s","select a,b from %s","select a,b from %s where a = 1","select a,b from %s where a = 1 and b = 2")
      val selectFormattedSQL : List[String] = selectTemplateSQL.map(x => x.format(addTableName))
      val selectParsedSQL    = selectFormattedSQL.map(x => selectStatement(x))
      val continuation : AQL.Continuation = (x => () )
      selectParsedSQL.map(x => x.get.apply(context,continuation) )
    }

    
    @Test def testUpdateStatement(){
  def updateStatement(in : String)= AQL.Parser.parseAll(AQL.Parser.updateStatement,in)
      val updateTemplateSQL  : List[String] = List("update %s set a = sum(1,a)","update %s set a = sum(1,a) where a = 1","update %s set a = sum(1,a) , b = 2 where a = 1")
      val updateFormattedSQL : List[String] = updateTemplateSQL.map(x => x.format(addTableName))
      val updateParsedSQL    = updateFormattedSQL.map(x => updateStatement(x))
      val continuation : AQL.Continuation = (x => () )
      updateParsedSQL.map(x => x.get.apply(context,continuation) )
    }

    @Test def testDeleteStatement(){
      def deleteStatement(in : String)= AQL.Parser.parseAll(AQL.Parser.deleteStatement,in)
      val deleteTemplateSQL  : List[String] = List("delete from %s","delete from %s where true","delete from %s where a = 1")
      val deleteFormattedSQL : List[String] = deleteTemplateSQL.map(x => x.format(addTableName))
      val deleteParsedSQL    = deleteFormattedSQL.map(x => deleteStatement(x))
      val continuation : AQL.Continuation = (x => () )
      deleteParsedSQL.map(x => x.get.apply(context,continuation) )
    }

    @Before  def before { AspectContext.register(classOf[org.blockfreie.helium.feuerkrake.test.TestA]) }
    @Test def testCallStatement(){
      def callStatement(in : String)= AQL.Parser.parseAll(AQL.Parser.callStatement,in)
      val callTemplateSQL  : List[String] = List("call sum(1,2)","call 1")
      val callFormattedSQL : List[String] = callTemplateSQL.map(x => x.format(addTableName))
      val callParsedSQL    = callFormattedSQL.map(x => callStatement(x))
      var actual : List[Long] = List();
      var expected : List[Long] = List(1L,3L);
      val continuation : AQL.Continuation = (x => actual = x.asInstanceOf[Long]::actual )
      callParsedSQL.map(x => x.get.apply(context,continuation) )
      Assert.assertEquals(expected,actual)
    }
    
    @Test def testInsertStatement(){
      def insertStatement(in : String)= AQL.Parser.parseAll(AQL.Parser.insertStatement,in)
      val insertSQL : String = "insert into %s (a,b) values (1,2)".format(addTableName)
      val expected : Long = 3L
      var actual   : Long = 0L
      val continuation : AQL.Continuation = (x => (actual = x.asInstanceOf[Long]) )
      insertStatement(insertSQL).get.apply(context,continuation)
      Assert.assertEquals(expected,actual)
    }

    @Test def testTupleExpression(){
      def tupleExpression(in : String)= AQL.Parser.parseAll(AQL.Parser.tupleExpression,in)
      Assert.assertEquals(tupleExpression("1").get.map(f => f(context)),List(1))
      Assert.assertEquals(tupleExpression("1,true,sum(1,1),true=true").get.map(f => f(context)),List(1,true,2L,true))
    }

    @Test def testTupleLabel(){
      def tupleLabel(in : String)= AQL.Parser.parseAll(AQL.Parser.tupleLabel,in)
      Assert.assertEquals(tupleLabel("a1,a2,a3,b1,b2,b3").get,List[String]("a1","a2","a3","b1","b2","b3"))
      Assert.assertEquals(tupleLabel("a1,a2,a3").get,List[String]("a1","a2","a3"))
      Assert.assertEquals(tupleLabel("a1").get,List[String]("a1")) 
    }

    @Test def expression(){
      def expression(in : String)= AQL.Parser.parseAll(AQL.Parser.expression,in)
      Assert.assertEquals(1L,expression("ab").get(context))
      Assert.assertEquals("a",expression(""""a"""").get(context))
      Assert.assertEquals(1L,expression("1").get(context))
      Assert.assertEquals(1.11D,expression("1.11").get(context))
      Assert.assertEquals(true,expression("true").get(context))
      Assert.assertEquals(false,expression("false").get(context))
      Assert.assertEquals(null,expression("Null").get(context))
      Assert.assertTrue(expression("true or true").get(context).asInstanceOf[Boolean])  
      Assert.assertTrue(expression("true and true").get(context).asInstanceOf[Boolean])  
      Assert.assertTrue(expression("1 = 1").get(context).asInstanceOf[Boolean])  
      Assert.assertTrue(expression("true and (1 = 1)").get(context).asInstanceOf[Boolean])
      Assert.assertTrue(expression("(true and (1 = 1)) or false").get(context).asInstanceOf[Boolean]) 
      Assert.assertTrue(expression("(sum(1,(sum(1,1))) = 3) or false").get(context).asInstanceOf[Boolean])
    }
    @Test def testOrExpression(){
      def orExpression(in : String)= AQL.Parser.parseAll(AQL.Parser.orExpression,in)
      Assert.assertTrue(orExpression("true or true").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(orExpression("true and False").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(orExpression("true and False and True").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(orExpression("False and False and False").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(orExpression("False").get(context).asInstanceOf[Boolean])  
    }
    @Test def testAndExpression(){
      def andExpression(in : String)= AQL.Parser.parseAll(AQL.Parser.andExpression,in)
      Assert.assertTrue(andExpression("true and true").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(andExpression("true and False").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(andExpression("true and true and False").get(context).asInstanceOf[Boolean])  
      Assert.assertFalse(andExpression("False").get(context).asInstanceOf[Boolean])  
    }

    @Test def testEqualExpression(){
      def equalExpression(in : String)= AQL.Parser.parseAll(AQL.Parser.equalExpression,in)
      Assert.assertTrue(equalExpression("1 = 1").get(context).asInstanceOf[Boolean])  
      Assert.assertEquals(1L,equalExpression("1 ").get(context).asInstanceOf[Long])  
      Assert.assertFalse(equalExpression("sum(1,2) = 2").get(context).asInstanceOf[Boolean])  
      Assert.assertTrue(equalExpression("sum(1,2) = 3").get(context).asInstanceOf[Boolean])  
      Assert.assertEquals(1L,equalExpression("one(1)").get(context).asInstanceOf[Long])  
    }
    @Test def testFunctionExpression(){
      def functionExpression(in : String)= AQL.Parser.parseAll(AQL.Parser.functionExpression,in)
      Assert.assertEquals(1L,functionExpression("one(1)").get(context))
      Assert.assertEquals(3L,functionExpression("sum(1,2)").get(context))
  }

    
    @Test def testID(){
      def id ( in : String) = AQL.Parser.parseAll(AQL.Parser.id,in)
      Assert.assertFalse(id("123").successful)
      Assert.assertTrue(id("a123").successful)
      Assert.assertEquals(id("a123").get,"a123")
    }
    testID()

    @Test def testString(){
      def string (in : String) = AQL.Parser.parseAll(AQL.Parser.string,in)
      Assert.assertFalse(string("a").successful)
      Assert.assertTrue(string(""""a"""").successful)
      Assert.assertEquals("a",string(""""a"""").get)
    }
    testString()

    @Test def testNumber(){
      def number(in : String) = AQL.Parser.parseAll(AQL.Parser.number,in)
      Assert.assertEquals(1L,number("1").get)
      Assert.assertEquals(-1L,number("-1").get)

      Assert.assertEquals(1.1D,number("1.1").get)
      Assert.assertEquals(-1.1D,number("-1.1").get)
      Assert.assertEquals(1.1D,number("+1.1").get)

      Assert.assertEquals(110D,number("1.1e2").get)
      Assert.assertEquals(-110D,number("-1.1e2").get)
      Assert.assertEquals(110D,number("+1.1e2").get)
    }
    testNumber()
    
    @Test def testValue(){
      def value(in : String) = AQL.Parser.parseAll(AQL.Parser.value,in)
      Assert.assertEquals(1L,value("ab").get(context))
      Assert.assertEquals("a",value(""""a"""").get(context))
      Assert.assertEquals(1L,value("1").get(context))
      Assert.assertEquals(1.11D,value("1.11").get(context))
      Assert.assertEquals(true,value("true").get(context))
      Assert.assertEquals(false,value("false").get(context))
      Assert.assertEquals(null,value("Null").get(context))
    }

  }
}
