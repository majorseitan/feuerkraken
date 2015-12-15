import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import  org.blockfreie.helium.feuerkrake._
import  org.blockfreie.helium.feuerkrake.JDBC._
package org.blockfreie.helium.feuerkrake.JDBC {
  class TestJDBC {
    @Test (expected = classOf[java.sql.SQLDataException])
    def failsWithoutNext()
    { val resultSet : ResultSet = new ResultSet()
      resultSet.getObject("test")
    }

    @Test (expected = classOf[java.sql.SQLDataException])
    def failsToFetchColumn()
    { val resultSet    : ResultSet = new ResultSet()
      Assert.assertEquals(None,resultSet.row)
      val continuation : AQL.Continuation = resultSet.getContinuation()
      continuation(Map())
      Assert.assertTrue(resultSet.next())
      Assert.assertEquals(Some(Map()),resultSet.row)
      resultSet.getColumn("1");
    }

    @Test
    def canGetColumn()
    { val resultSet    : ResultSet = new ResultSet()
      Assert.assertEquals(None,resultSet.row)
      val continuation : AQL.Continuation = resultSet.getContinuation()
      continuation(Map())
      Assert.assertTrue(resultSet.next())
      Assert.assertEquals(Some(Map()),resultSet.row)
    }

    def createResultSet(row : Map[String,Any]) : ResultSet = {
      val resultSet    : ResultSet = new ResultSet()
      val continuation : AQL.Continuation = resultSet.getContinuation()
      continuation(row)
      resultSet.next()
      resultSet
    }
    def canGetObject()
    { var uuid : java.util.UUID = java.util.UUID.randomUUID()
      var resultSet : ResultSet = createResultSet(Map[String,Any]("1"->uuid))
      Assert.assertEquals(uuid,resultSet.getObject(1))
      Assert.assertEquals(uuid,resultSet.getObject("1"))
    }

    def sampleNumericMap() : Map[String,Any] = {
      Map("1" -> new java.math.BigDecimal("1")
         ,"2" -> 2.toDouble
         ,"3" -> 3.toInt
         ,"4" -> 4.toLong
         ,"5" -> 5.toByte
         ,"6" -> 6.toShort
         ,"7" -> 7.toChar
         ,"8" -> 8.toFloat
         ,"9" -> "9")
    }
    @Test
    def canGetBigDecimial()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(new java.math.BigDecimal(1),resultSet.getBigDecimal(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => 0 == new java.math.BigDecimal(key.toInt).compareTo(resultSet.getBigDecimal(key)) })
    }

    @Test
    def canGetBoolean()
    { var resultSet : ResultSet = createResultSet(Map[String,Any]("1"->true))
      Assert.assertEquals(true,resultSet.getBoolean(1))
      Assert.assertEquals(true,resultSet.getBoolean("1"))
    }
    @Test
    def canGetByte()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toByte,resultSet.getByte(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toByte == resultSet.getByte(key) })
    }
    @Test
    def canGetDouble()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toDouble,resultSet.getDouble(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toDouble == resultSet.getDouble(key) })
    }
    @Test
    def canGetFloat()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toFloat,resultSet.getFloat(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toFloat == resultSet.getFloat(key) })
    }
    @Test
    def canGetInt()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toInt,resultSet.getInt(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toInt == resultSet.getInt(key) })
    }
    @Test
    def canGetLong()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toLong,resultSet.getLong(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toLong == resultSet.getLong(key) })
    }
    @Test
    def canGetShort()
    { var resultSet : ResultSet = createResultSet(sampleNumericMap())
      Assert.assertEquals(1.toShort,resultSet.getShort(1))
      Assert.assertTrue(sampleNumericMap().forall  { case (key, value) => key.toShort == resultSet.getShort(key) })
    }


    @Test
    def canGetBytes()
    { var resultSet : ResultSet = createResultSet(Map[String,Any]("1"->Array[Byte](1.toByte,2.toByte)))
      Assert.assertTrue(Array[Byte](1.toByte,2.toByte).sameElements(resultSet.getBytes(1)))
      Assert.assertTrue(Array[Byte](1.toByte,2.toByte).sameElements(resultSet.getBytes("1")))
    }
    def sampleDateMap(date : java.util.Date) : Map[String,Any] = {
      Map("1" -> date
         ,"2" -> new java.sql.Time(new java.util.Date().getTime())
         ,"3" -> new java.sql.Timestamp(new java.util.Date().getTime()))
    }
    /*
    @Test
    def canGetDate()
    { var date : java.util.Date = new java.text.SimpleDateFormat("dd-MM-yyyy").parse("15-01-2012")
      var resultSet : ResultSet = createResultSet(sampleDateMap(date))
      Assert.assertEquals(date,resultSet.getDate(1))
      Assert.assertTrue(sampleDateMap(date).forall{ case (key, value) => scala.math.abs(date.getTime-resultSet.getDate(key).getTime) < 15000800000L }  )
    }
    @Test
    def canGetTime()
    { var date : java.util.Date = new java.text.SimpleDateFormat("dd-MM-yyyy").parse("15-01-2012")
      var resultSet : ResultSet = createResultSet(sampleDateMap(date))
      Assert.assertEquals(date,resultSet.getTime(1))
      Assert.assertTrue(sampleDateMap(date).forall{ case (key, value) => scala.math.abs(date.getTime-resultSet.getTime(key).getTime) < 15000800000L }  )
    }
    @Test
    def canGetTimestamp()
    { var date : java.util.Date = new java.text.SimpleDateFormat("dd-MM-yyyy").parse("15-01-2012")
      var resultSet : ResultSet = createResultSet(sampleDateMap(date))
      Assert.assertEquals(date,resultSet.getTimestamp(1))
      Assert.assertTrue(sampleDateMap(date).forall{ case (key, value) => scala.math.abs(date.getTime-resultSet.getTimestamp(key).getTime) < 15000800000L }  )
    }
    */
    def canGetURL()
    { var surl : String = "http://www.cnn.com"
      var url  : java.net.URL = new java.net.URL(surl)
      var resultSet : ResultSet = createResultSet(Map[String,Any]("1"->surl,"2"->url))
      Assert.assertEquals(url,resultSet.getURL(1))
      Assert.assertEquals(url,resultSet.getURL(2))
    }
  }
}
