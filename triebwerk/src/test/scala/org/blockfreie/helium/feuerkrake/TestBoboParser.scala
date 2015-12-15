import  org.blockfreie.helium.feuerkrake._
package org.blockfreie.helium.feuerkrake{
  import org.blockfreie.helium.feuerkrake._
  import org.blockfreie.helium.feuerkrake.annotation._
  import junit.framework.Assert
  import org.junit._;
  import java.util.logging.Logger
  import java.util.logging.Level
  class  TestBoboParser{
    val LOGGER : Logger = Logger.getLogger("org.blockfreie.helium.feuerkrake.TestBoboParser");
    val random : scala.util.Random = new scala.util.Random
    def randomTuple : (Long,Long) = (scala.math.abs(random.nextLong),scala.math.abs(random.nextLong))
    val addTableName : String = org.blockfreie.helium.feuerkrake.test.TestA.ADD_TABLE_NAME
    @Before  def before {
      AspectContext.register(classOf[org.blockfreie.helium.feuerkrake.test.TestA])
    }
    @Test def canInsert = {
      val insertSQL : String = "insert %s a:1,b:2".format(addTableName)
      val expected : Long = 3L
      var actual   : Long = 0L
      BoboParser.run(insertSQL).get.apply((x => (actual = x.asInstanceOf[Long]) ))
      Assert.assertEquals(expected,actual)
    }
    @Test def canSelect = {
      LOGGER.log(Level.INFO,org.blockfreie.helium.feuerkrake.aspect.AspectEmitter.getHandler.toString)
      val selectSQL : String = "select %s".format(addTableName)
      val (aValue,bValue) : (Long,Long)   = randomTuple
      val insertSQL : String = "insert %s a:%d,b:%d".format(addTableName,aValue,bValue)
      var actual    : Map[String,Long] = Map[String,Long]()
      val expected  : Map[String,Long] = Map[String,Long]("a" -> aValue,"b" -> bValue)
      BoboParser.run(selectSQL).get.apply((x => {LOGGER.log(Level.INFO,x.toString);actual = x.asInstanceOf[Map[String,Long]]}))
      BoboParser.run(insertSQL).get.apply((x => () ))
      Assert.assertTrue(expected.forall(x => {actual(x._1) == x._2}))
    }
    @Test def update = {
       val (aValue,bValue)  = randomTuple
       val insertSQL : String = "insert %s a:1,b:2".format(addTableName)
       val selectSQL : String = "select %s".format(addTableName)
       val updateSQL : String = "update %s a:%d,b:%d".format(addTableName,aValue,bValue)
       var actual    : Map[String,Long] = Map[String,Long]()
       val expected  : Map[String,Long] = Map[String,Long]("a" -> aValue,"b" -> bValue)
       BoboParser.run(selectSQL).get.apply((x => (actual = x.asInstanceOf[Map[String,Long]])))
       BoboParser.run(updateSQL).get.apply((x => () ))
       BoboParser.run(insertSQL).get.apply((x => () ))
       Assert.assertTrue(expected.forall(x => {actual(x._1) == x._2}))
    }

    @Test def delete = {
      val valueA    : (Long,Long) = randomTuple
      val valueB    : (Long,Long) = (valueA._1+1,valueA._2+1)
      val insertSQLA: String = "insert %s a:%d,b:%d".format(addTableName,valueA._1,valueA._2)
      val insertSQLB: String = "insert %s a:%d,b:%d".format(addTableName,valueB._1,valueB._2)
      val selectSQL : String = "select %s".format(addTableName)
      val deleteSQL : String = "delete %s a:%d,b:%d".format(addTableName,valueA._1,valueA._2)
      var expectedA : Map[String,Long] = Map[String,Long]("a" -> valueA._1,"b" -> valueA._2)
      var expectedB : Map[String,Long] = Map[String,Long]("a" -> valueB._1,"b" -> valueB._2)
      var actual    : Map[String,Long] = Map[String,Long]()
      val empty     : Map[String,Long] = Map[String,Long]()
      BoboParser.run(selectSQL).get.apply((x => (actual = x.asInstanceOf[Map[String,Long]])))
      BoboParser.run(insertSQLA).get.apply((x => () ))
      Assert.assertTrue(expectedA.forall(x => {actual(x._1) == x._2}))

      BoboParser.run(insertSQLB).get.apply((x => () ))
      Assert.assertTrue(expectedB.forall(x => {actual(x._1) == x._2}))

      BoboParser.run(deleteSQL).get.apply((x => () ))

      actual = Map[String,Long]() 
      BoboParser.run(insertSQLA).get.apply((x => () ))
      Assert.assertEquals(empty.size,actual.size)
      Assert.assertTrue(empty == actual)
      actual = Map[String,Long]()
      BoboParser.run(insertSQLB).get.apply((x => () ))
      Assert.assertTrue(expectedB.forall(x => {actual(x._1) == x._2}))

    }
  }
}
