import org.blockfreie.helium.feuerkrake._
import java.sql.SQLClientInfoException
import java.util.logging.Logger
import java.util.logging.Level
import org.blockfreie.element.hydrogen.murikate.LogUtil
import java.util.concurrent.LinkedBlockingQueue

package org.blockfreie.helium.feuerkrake.JDBC {
  class ResultSet extends java.sql.ResultSet {
    val LOGGER : Logger = LogUtil.createLogger(Level.ALL)

     def absolute(row : Int) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def afterLast() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def beforeFirst() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def cancelRowUpdates() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def clearWarnings() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def close() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def deleteRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def findColumn(columnName : String) : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def first() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def getArray(columnIndex : Int) : java.sql.Array = throw new java.sql.SQLFeatureNotSupportedException()
     def getArray(colName : String) : java.sql.Array = throw new java.sql.SQLFeatureNotSupportedException()
     def getAsciiStream(columnIndex : Int) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getAsciiStream(columnName : String) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getBigDecimal(columnIndex : Int) : java.math.BigDecimal = getBigDecimal(columnIndex.toString)
     def getBigDecimal(columnIndex : Int,scale : Int) : java.math.BigDecimal = getBigDecimal(columnIndex.toString)
     def getBigDecimal(columnName : String) : java.math.BigDecimal = {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x
        case x:Double         => new java.math.BigDecimal(x)
        case x:Int            => new java.math.BigDecimal(x)
        case x:Long           => new java.math.BigDecimal(x)
        case x:String         => new java.math.BigDecimal(x)
        case x:Byte           => new java.math.BigDecimal(x.toDouble)
        case x:Short          => new java.math.BigDecimal(x.toInt)
        case x:Char           => new java.math.BigDecimal(x.toInt)
        case x:Float          => new java.math.BigDecimal(x.toString)
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.math.BigDecimal].getName))
      }
    }
     def getBigDecimal(columnName : String,scale : Int) : java.math.BigDecimal = getBigDecimal(columnName)
     def getBinaryStream(columnIndex : Int) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getBinaryStream(columnName : String) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getBlob(columnIndex : Int) : java.sql.Blob = throw new java.sql.SQLFeatureNotSupportedException()
     def getBlob(columnName : String) : java.sql.Blob = throw new java.sql.SQLFeatureNotSupportedException()
     def getBoolean(columnIndex : Int) : Boolean = getBoolean(columnIndex.toString)
     def getBoolean(columnName : String) : Boolean = {
      getColumn(columnName) match {
        case x:java.lang.Boolean => x
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.lang.Boolean].getName))
      }
    }
     def getByte(columnIndex : Int) : Byte = getByte(columnIndex.toString)
     def getByte(columnName : String) : Byte = {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.intValue.toByte
        case x:Double         => x.toByte
        case x:Int            => x.toByte
        case x:Long           => x.toByte
        case x:String         => x.toByte
        case x:Byte           => x
        case x:Short          => x.toByte
        case x:Char           => x.toByte
        case x:Float          => x.toByte
        case x                => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.lang.Byte].getName))
      }
    }

     def getBytes(columnIndex : Int) : Array[Byte] = getBytes(columnIndex.toString)
     def getBytes(columnName : String) : Array[Byte] = {
      getColumn(columnName) match {
        case x:Array[Byte] => x
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[Array[Byte]].getName))
      }
    }
     def getCharacterStream(columnIndex : Int) : java.io.Reader = throw new java.sql.SQLFeatureNotSupportedException()
     def getCharacterStream(columnName : String) : java.io.Reader = throw new java.sql.SQLFeatureNotSupportedException()
     def getClob(columnIndex : Int) : java.sql.Clob = throw new java.sql.SQLFeatureNotSupportedException()
     def getClob(columnName : String) : java.sql.Clob = throw new java.sql.SQLFeatureNotSupportedException()
     def getConcurrency() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getCursorName() : String = throw new java.sql.SQLFeatureNotSupportedException()
     def getDate(columnIndex : Int) : java.sql.Date = getDate(columnIndex.toString,java.util.Calendar.getInstance())
     def getDate(columnIndex : Int,cal : java.util.Calendar) : java.sql.Date = getDate(columnIndex.toString,cal)
     def getDate(columnName : String) = getDate(columnName,java.util.Calendar.getInstance())
     def getDate(columnName : String,cal : java.util.Calendar = java.util.Calendar.getInstance()) : java.sql.Date = {
      getColumn(columnName) match {
        case x:java.sql.Date  => x
        case x:java.util.Date => {
          cal.setTime(x);
          cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
          cal.set(java.util.Calendar.MINUTE, 0);
          cal.set(java.util.Calendar.SECOND, 0);
          cal.set(java.util.Calendar.MILLISECOND, 0);
          new java.sql.Date(cal.getTime().getTime())
        }
        case _           => {
          cal.setTimeInMillis(getLong(columnName));
          cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
          cal.set(java.util.Calendar.MINUTE, 0);
          cal.set(java.util.Calendar.SECOND, 0);
          cal.set(java.util.Calendar.MILLISECOND, 0);
          new java.sql.Date(cal.getTime().getTime())
        }
      }
    }
     def getDouble(columnIndex : Int) : Double = getDouble(columnIndex.toString)
     def getDouble(columnName : String) : Double = {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.doubleValue
        case x:Double               => x
        case x:Int                  => x.toDouble
        case x:Long                 => x.toDouble
        case x:String               => x.toDouble
        case x:Byte                 => x.toDouble
        case x:Short                => x.toDouble
        case x:Char                 => x.toByte
        case x:Float          => x.toByte
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.sql.Date].getName))
      }
    }
     def getFetchDirection() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getFetchSize() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getFloat(columnIndex : Int) : Float = getFloat(columnIndex.toString)
     def getFloat(columnName : String) : Float =  {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.floatValue
        case x:Double               => x.toFloat
        case x:Int                  => x.toFloat
        case x:Long                 => x.toFloat
        case x:String               => x.toFloat
        case x:Byte                 => x.toFloat
        case x:Short                => x.toFloat
        case x:Char                 => x.toFloat
        case x:Float                => x
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.sql.Date].getName))
      }
    }
     def getHoldability() : Int  = throw new java.sql.SQLFeatureNotSupportedException()
     def getInt(columnIndex : Int) : Int = getInt(columnIndex.toString)
     def getInt(columnName : String) : Int = {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.intValue
        case x:Double         => x.toInt
        case x:Int            => x
        case x:Long           => x.toInt
        case x:String         => x.toInt
        case x:Byte           => x.toInt
        case x:Short          => x.toInt
        case x:Char           => x.toInt
        case x:Float          => x.toInt
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.sql.Date].getName))
      }
    }
     def getLong(columnIndex : Int) : Long = getLong(columnIndex.toString)
     def getLong(columnName : String) : Long =  {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.longValue
        case x:Double         => x.toLong
        case x:Int            => x.toLong
        case x:Long           => x
        case x:String         => x.toLong
        case x:Byte           => x.toLong
        case x:Short          => x.toLong
        case x:Char           => x.toLong
        case x:Float          => x.toLong
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.sql.Date].getName))
      }
    }
     def getMetaData() : java.sql.ResultSetMetaData = throw new java.sql.SQLFeatureNotSupportedException()
     def getNCharacterStream(columnIndex : Int) : java.io.Reader = throw new java.sql.SQLFeatureNotSupportedException()
     def getNCharacterStream(columnName : String) : java.io.Reader = throw new java.sql.SQLFeatureNotSupportedException()
     def getNClob(columnIndex : Int) : java.sql.NClob = throw new java.sql.SQLFeatureNotSupportedException()
     def getNClob(columnName : String) : java.sql.NClob = throw new java.sql.SQLFeatureNotSupportedException()
     def getNString(columnIndex : Int) : String  = throw new java.sql.SQLFeatureNotSupportedException()
     def getNString(columnName : String) : String = throw new java.sql.SQLFeatureNotSupportedException()
     def getObject(columnIndex : Int) : Object = getObject(columnIndex.toString)
     def getObject(columnIndex : Int,map : java.util.Map[String,Class[_]]) : Object = throw new java.sql.SQLFeatureNotSupportedException()
     def getObject(columnName : String) : Object = getColumn(columnName).asInstanceOf[Object]
     def getObject(columnName : String,map : java.util.Map[String,Class[_]]) : Object = throw new java.sql.SQLFeatureNotSupportedException()
     def getObject[T](columnIndex : Int,t : java.lang.Class[T]) : T = getObject[T](columnIndex.toString,t)
     def getObject[T](columnName : String,t : java.lang.Class[T]) : T = getColumn(columnName).asInstanceOf[T]
     def getRef(columnIndex : Int) : java.sql.Ref = throw new java.sql.SQLFeatureNotSupportedException()
     def getRef(columnName : String) : java.sql.Ref = throw new java.sql.SQLFeatureNotSupportedException()
     def getRow() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getRowId(columnIndex : Int) : java.sql.RowId  = throw new java.sql.SQLFeatureNotSupportedException()
     def getRowId(columnName : String) : java.sql.RowId  = throw new java.sql.SQLFeatureNotSupportedException()
     def getShort(columnIndex : Int) : Short = getShort(columnIndex.toString)
     def getShort(columnName : String) : Short = {
      getColumn(columnName) match {
        case x:java.math.BigDecimal => x.intValue.toShort
        case x:Double         => x.toShort
        case x:Int            => x.toShort
        case x:Long           => x.toShort
        case x:String         => x.toShort
        case x:Byte           => x.toShort
        case x:Short          => x
        case x:Char           => x.toShort
        case x:Float          => x.toShort
        case x            => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.sql.Date].getName))
      }
    }
     def getStatement() : java.sql.Statement = throw new java.sql.SQLFeatureNotSupportedException()
     def getString(columnIndex : Int) : String = getString(columnIndex.toString)
     def getString(columnName : String) : String = {
      getColumn(columnName) match {
        case null => "null"
        case x    => x.toString
      }
    }
     def getSQLXML(columnIndex : Int) : java.sql.SQLXML = throw new java.sql.SQLFeatureNotSupportedException()
     def getSQLXML(columnName : String) : java.sql.SQLXML = throw new java.sql.SQLFeatureNotSupportedException()
     def getTime(columnIndex : Int) : java.sql.Time = getTime(columnIndex.toString)
     def getTime(columnIndex : Int,cal : java.util.Calendar) : java.sql.Time = getTime(columnIndex.toString,cal)
     def getTime(columnName : String) : java.sql.Time = getTime(columnName,java.util.Calendar.getInstance())
     def getTime(columnName : String,cal : java.util.Calendar) : java.sql.Time =  {
      getColumn(columnName) match {
        case x:java.sql.Time  => x
        case x:java.util.Date => {
          cal.setTime(x);
          new java.sql.Time(cal.getTime().getTime())
        }
        case _            => {
          cal.setTimeInMillis(getLong(columnName));
          new java.sql.Time(cal.getTime().getTime())
        }
      }
    }
     def getTimestamp(columnIndex : Int) : java.sql.Timestamp = getTimestamp(columnIndex.toString)
     def getTimestamp(columnIndex : Int,cal : java.util.Calendar) : java.sql.Timestamp = getTimestamp(columnIndex.toString,cal)
     def getTimestamp(columnName : String) : java.sql.Timestamp = getTimestamp(columnName,java.util.Calendar.getInstance())
     def getTimestamp(columnName : String,cal : java.util.Calendar) : java.sql.Timestamp = {
      getColumn(columnName) match {
        case x:java.sql.Timestamp  => x
        case x:java.util.Date => {
          cal.setTime(x);
          new java.sql.Timestamp(cal.getTime().getTime())
        }
        case _            => {
          cal.setTimeInMillis(getLong(columnName));
          new java.sql.Timestamp(cal.getTime().getTime())
        }
      }
    }
     def getType() : Int = java.sql.ResultSet.TYPE_FORWARD_ONLY
     def getUnicodeStream(columnIndex : Int) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getUnicodeStream(columnName : String) : java.io.InputStream = throw new java.sql.SQLFeatureNotSupportedException()
     def getURL(columnIndex : Int) : java.net.URL = getURL(columnIndex.toString)
     def getURL(columnName : String) : java.net.URL = {
      getColumn(columnName) match {
        case x:java.net.URL => x
        case x:String       => new java.net.URL(x)
        case x              => throw new java.sql.SQLException("cannot cast value %s of type %s to %s".format(x,x.getClass,classOf[java.net.URL].getName))
      }
    }
     def getWarnings() : java.sql.SQLWarning = throw new java.sql.SQLFeatureNotSupportedException()
     def insertRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def isAfterLast() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isBeforeFirst() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isClosed() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isFirst() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isLast() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isWrapperFor(iface : java.lang.Class[_]) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def last() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def moveToCurrentRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def moveToInsertRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def next() : Boolean = {
      val any = queue.take();
      row = Some(any.asInstanceOf[Map[String,Any]])
      true
    }
     def previous() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def refreshRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def relative(row : Int) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def rowDeleted() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def rowInserted() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def rowUpdated() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def setFetchDirection(direction : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setFetchSize(rows : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateArray(columnIndex : Int,array : java.sql.Array) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateArray(columnName : String,array : java.sql.Array) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnIndex : Int,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnIndex : Int,inputStream : java.io.InputStream, length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnIndex : Int,inputStream : java.io.InputStream, length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnName : String,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnName : String,inputStream : java.io.InputStream,length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateAsciiStream(columnName : String,inputStream : java.io.InputStream,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBigDecimal(columnIndex : Int,bigDecimal : java.math.BigDecimal) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBigDecimal(columnName : String,bigDecimal : java.math.BigDecimal) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnIndex : Int,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnIndex : Int,inputStream : java.io.InputStream,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnIndex : Int,inputStream : java.io.InputStream,length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnName : String,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnName : String,inputStream : java.io.InputStream,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBinaryStream(columnName : String,inputStream : java.io.InputStream,length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnIndex : Int,blob : java.sql.Blob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnIndex : Int,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnIndex : Int,inputStream : java.io.InputStream,long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnName : String,blob : java.sql.Blob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnName : String,inputStream : java.io.InputStream) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBlob(columnName : String,inputStream : java.io.InputStream,long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBoolean(columnIndex : Int,boolean : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBoolean(columnName : String,boolean : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateByte(columnIndex : Int,byte :Byte) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateByte(columnName : String,byte : Byte) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBytes(columnIndex : Int,bytes : Array[Byte]) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateBytes(columnName : String,bytes : Array[Byte]) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnIndex : Int,reader : java.io.Reader, length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnIndex : Int,reader : java.io.Reader, length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnIndex : Int,reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnName : String,reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnName : String,reader : java.io.Reader, length : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateCharacterStream(columnName : String,reader : java.io.Reader, length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnIndex : Int, clob : java.sql.Clob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnIndex : Int, reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnIndex : Int, reader : java.io.Reader,long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnName : String, clob : java.sql.Clob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnName : String, reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateClob(columnName : String, reader : java.io.Reader,long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateDate(columnIndex : Int,date : java.sql.Date) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateDate(columnName : String,date : java.sql.Date) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateDouble(columnIndex : Int,double : Double) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateDouble(columnName : String,double : Double) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateFloat(columnIndex : Int, float : Float) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateFloat(columnName : String, float : Float) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateInt(columnIndex : Int, int : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateInt(columnName : String, int : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateLong(columnIndex : Int, long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateLong(columnName : String, long : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNCharacterStream(columnIndex : Int,reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNCharacterStream(columnName : String,reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNCharacterStream(columnIndex : Int, reader : java.io.Reader,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNCharacterStream(columnName : String, reader : java.io.Reader,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnIndex : Int,nClob : java.sql.NClob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnIndex : Int, reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnIndex : Int, reader : java.io.Reader,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnLabel : String, nClob : java.sql.NClob) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnLabel : String, reader : java.io.Reader) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNClob(columnLabel : String, reader : java.io.Reader,length : Long) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNString(columnIndex : Int,nString : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNString(columnName : String,nString : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNull(columnIndex : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateNull(columnName : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateObject(columnIndex : Int, obj : Object) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateObject(columnIndex : Int,obj : Object,scale : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateObject(columnName : String, obj : Object) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateObject(columnName : String, obj : Object, scale : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateRef(columnIndex : Int,ref : java.sql.Ref) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateRef(columnName : String,ref : java.sql.Ref) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateRow() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateRowId(columnIndex : Int,rowId : java.sql.RowId) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateRowId(columnName : String,rowId : java.sql.RowId) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateShort(columnIndex : Int,short : Short) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateShort(columnName : String,short : Short) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateString(columnIndex : Int,string : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateString(columnName : String,string : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateSQLXML(columnName : String,sqlxml : java.sql.SQLXML) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateSQLXML(columnIndex : Int,sqlxml : java.sql.SQLXML) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateTime(columnIndex : Int,time : java.sql.Time) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateTime(columnName : String,time : java.sql.Time) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateTimestamp(columnIndex : Int,timestamp : java.sql.Timestamp) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def updateTimestamp(columnName : String,timestamp : java.sql.Timestamp) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def unwrap[T](iface : java.lang.Class[T]) : T = throw new java.sql.SQLFeatureNotSupportedException()
     def wasNull() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()

    var row:Option[Map[String,Any]] = None
    val queue : LinkedBlockingQueue[Any] = new LinkedBlockingQueue[Any]()
    def getContinuation() : AQL.Continuation = { x => queue.offer(x) }
    def getColumn(columnIndex : Int) : Any = getColumn(columnIndex.toString)
    def getColumn(columnName : String) : Any = {
      row match {
        case None => { throw new java.sql.SQLDataException("Invalid cursor state - no current row.") }
        case Some(rowMap) => {
          if(rowMap.contains(columnName))
            { rowMap(columnName) }
          else
            { throw new java.sql.SQLDataException("could not find column : %s in row : %s".format(columnName,rowMap)); }
        }
      }
    }
  }

  class Statement(val context : AQL.Context) extends java.sql.Statement {
    val LOGGER : Logger = LogUtil.createLogger(Level.ALL)

    def executeContinuation(sql : String) : AQL.Continuation = { x => LOGGER.log(Level.INFO,"%s : %s ".format(sql,x)) }

     def addBatch(sql : String) : Unit =  throw new java.sql.SQLFeatureNotSupportedException()
     def cancel() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def clearBatch() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def clearWarnings() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def close() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def closeOnCompletion() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def execute(sql : String) : Boolean = { AQL.Parser.run(sql).get.apply(context,executeContinuation(sql)); true  }   // TODO - fix exception handling
     def execute(sql : String,autoGeneratedKeys : Int) : Boolean = execute(sql)
     def execute(sql : String, columnIndexes : Array[Int]) : Boolean = execute(sql)
     def execute(sql : String, columnNames : Array[String]) : Boolean =  execute(sql)
     def executeBatch() : Array[Int] = throw new java.sql.SQLFeatureNotSupportedException()       // TODO
     def executeQuery(sql : String)  : java.sql.ResultSet = { val result = new ResultSet(); AQL.Parser.run(sql).get.apply(context,result.getContinuation()); result }
     def executeUpdate(sql : String) : Int = { execute(sql); 0 }
     def executeUpdate(sql : String, autoGeneratedKeys : Int) : Int  = executeUpdate(sql)
     def executeUpdate(sql : String, columnIndexes : Array[Int]) : Int = executeUpdate(sql)
     def executeUpdate(sql : String, columnNames : Array[String]) : Int = executeUpdate(sql)
     def getConnection() : java.sql.Connection = throw new java.sql.SQLFeatureNotSupportedException()
     def getFetchDirection() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getFetchSize() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getGeneratedKeys() : java.sql.ResultSet = throw new java.sql.SQLFeatureNotSupportedException()
     def getMaxFieldSize() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getMaxRows() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getMoreResults() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def getMoreResults(current : Int) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def getQueryTimeout() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getResultSet() : java.sql.ResultSet = throw new java.sql.SQLFeatureNotSupportedException()
     def getResultSetConcurrency() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getResultSetHoldability() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getResultSetType() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getUpdateCount() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getWarnings() : java.sql.SQLWarning = throw new java.sql.SQLFeatureNotSupportedException()
     def isClosed() : Boolean  = throw new java.sql.SQLFeatureNotSupportedException()
     def isCloseOnCompletion() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isPoolable() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isWrapperFor(iface : java.lang.Class[_]) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def setCursorName(name : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setEscapeProcessing(enable : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setFetchDirection(direction : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setFetchSize(rows : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setMaxFieldSize(max : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setMaxRows(max : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setPoolable(poolable : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setQueryTimeout(seconds : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def unwrap[T](iface : java.lang.Class[T]) : T = throw new java.sql.SQLFeatureNotSupportedException()
  }

  class Connection(url : java.lang.String) extends java.sql.Connection {
    val context : AQL.Context = new AQL.Context()

     def abort(executor : java.util.concurrent.Executor) : Unit  = throw new java.sql.SQLFeatureNotSupportedException()
     def clearWarnings() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def close() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def commit() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def createNClob() : java.sql.NClob = throw new java.sql.SQLFeatureNotSupportedException()
     def createSQLXML() : java.sql.SQLXML = throw new java.sql.SQLFeatureNotSupportedException()
     def createClob() : java.sql.Clob = throw new java.sql.SQLFeatureNotSupportedException()
     def createBlob() : java.sql.Blob = throw new java.sql.SQLFeatureNotSupportedException()
     def createStatement() : java.sql.Statement = new Statement(context)
     def createStatement(resultSetType : Int,resultSetConcurrency : Int) : java.sql.Statement = new Statement(context)
     def createStatement(resultSetType : Int,resultSetConcurrency : Int,resultSetHoldability : Int) : java.sql.Statement = new Statement(context)
     def createStruct(typeName : String,attributes : Array[Object]) : java.sql.Struct = throw new java.sql.SQLFeatureNotSupportedException()
     def createArrayOf(typeName : String,elements : Array[Object]) : java.sql.Array = throw new java.sql.SQLFeatureNotSupportedException()
     def getAutoCommit() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def getCatalog() : String = throw new java.sql.SQLFeatureNotSupportedException()
     def getClientInfo() : java.util.Properties = throw new java.sql.SQLFeatureNotSupportedException()
     def getClientInfo(name : String) = throw new java.sql.SQLFeatureNotSupportedException()
     def getHoldability() : Int  = throw new java.sql.SQLFeatureNotSupportedException()
     def getMetaData() : java.sql.DatabaseMetaData = throw new java.sql.SQLFeatureNotSupportedException()
     def getNetworkTimeout() : Int = throw new java.sql.SQLFeatureNotSupportedException()
     def getSchema() : String = throw new java.sql.SQLFeatureNotSupportedException()
     def getTransactionIsolation() : Int =  throw new java.sql.SQLFeatureNotSupportedException()
     def getTypeMap : java.util.Map[java.lang.String,java.lang.Class[_]] = throw new java.sql.SQLFeatureNotSupportedException()
     def getWarnings() : java.sql.SQLWarning =  throw new java.sql.SQLFeatureNotSupportedException()
     def isClosed() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isReadOnly() : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isValid(timeout : Int)  : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def isWrapperFor(iface : java.lang.Class[_]) : Boolean = throw new java.sql.SQLFeatureNotSupportedException()
     def nativeSQL(sql : String)  : String = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareCall(sql : String) : java.sql.CallableStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareCall(sql : String,resultSetType : Int,resultSetConcurrency : Int) : java.sql.CallableStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareCall(sql  : String,resultSetType : Int,resultSetConcurrency : Int,resultSetHoldability : Int) : java.sql.CallableStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String,columnNames : Array[String]) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String,columnIndexes : Array[Int]) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String,autoGeneratedKeys : Int) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String,resultSetType : Int,resultSetConcurrency : Int) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def prepareStatement(sql : String,resultSetType : Int,resultSetConcurrency : Int,resultSetHoldability : Int) : java.sql.PreparedStatement = throw new java.sql.SQLFeatureNotSupportedException()
     def releaseSavepoint(savepoint : java.sql.Savepoint) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def rollback() : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def rollback(savepoint : java.sql.Savepoint) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setAutoCommit(autoCommit : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setCatalog(catalog : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setClientInfo(properties : java.util.Properties) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setClientInfo(name : String,value : String ) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setHoldability(holdability : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setNetworkTimeout(executor : java.util.concurrent.Executor,milliseconds : Int) : Unit  = throw new java.sql.SQLFeatureNotSupportedException()
     def setReadOnly(readOnly : Boolean) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setSavepoint() : java.sql.Savepoint = throw new java.sql.SQLFeatureNotSupportedException()
     def setSavepoint(name : String) : java.sql.Savepoint = throw new java.sql.SQLFeatureNotSupportedException()
     def setSchema(schema : String) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setTypeMap(map : java.util.Map[java.lang.String,java.lang.Class[_]]) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def setTransactionIsolation(level : Int) : Unit = throw new java.sql.SQLFeatureNotSupportedException()
     def unwrap[T](iface : java.lang.Class[T]) : T = throw new java.sql.SQLFeatureNotSupportedException()
  }

  class Driver extends java.sql.Driver {
    /**
     * Check if this driver is compliant to the JDBC specification.
     * This method should not be called by an application.
     *
     * @return true
     */
    def jdbcCompliant() : Boolean = false
    /**
     * Get the minor version number of the driver.
     * This method should not be called by an application.
     *
     * @return the minor version number
     */
    def getMinorVersion() : Int   = 0
    /**
     * Get the major version number of the driver.
     * This method should not be called by an application.
     *
     * @return the major version number
     */
    def getMajorVersion() : Int   = 1
    /**
     * Get the list of supported properties.
     * This method should not be called by an application.
     *
     * @param url the database URL
     * @param info the connection properties
     * @return a zero length array
     */
    def getParentLogger() : java.util.logging.Logger  = throw new java.sql.SQLFeatureNotSupportedException()
    def getPropertyInfo(url : java.lang.String,info : java.util.Properties) : Array[java.sql.DriverPropertyInfo] = Array[java.sql.DriverPropertyInfo]()
    /**
     * Check if the driver understands this URL.
     * This method should not be called by an application.
     *
     * @param url the database URL
     * @return if the driver understands the URL
     */
    def acceptsURL(url : java.lang.String) : Boolean = true
    /**
     * Open a database connection.
     * This method should not be called by an application.
     * Instead, the method DriverManager.getConnection should be used.
     *
     * @param url the database URL
     * @param info the connection properties
     * @return the new connection or null if the URL is not supported
     */
    def connect(url : java.lang.String,info : java.util.Properties) : java.sql.Connection = new Connection(url)
  }
}
