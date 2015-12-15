package org.blockfreie.helium.feuerkrake{
  import scala.util.parsing.combinator._
  import java.util.logging.Logger
  import java.util.logging.Level

  object BoboHandler {
    val LOGGER : Logger = Logger.getLogger("org.blockfreie.helium.feuerkrake.BoboHandler");
    val columnWidth = 10;
    val maxColumns  = 10;
    val success : String      = "success\n"

    def formatRow(row : Row) = { 
      val names   : List[RowID] = row.data.toList.map(entry => entry._1).sortWith(_.id < _.id)
      val values  : List[Any]   = names.map(rowID => row.data(rowID))
      val numCol  : Int         = values.length
      val format  : String      = (1 to scala.math.min(numCol,maxColumns)).map(x =>"%%%1$d.%1$ds".format(columnWidth)).foldLeft("")(_ + "|" + _)+"|"+(if(numCol > maxColumns){"..."}else{""})
      val result  : String      = format.format(values.toArray:_*)
      result
    }
    type Continuation = (Any => Unit)
    def insertStatement    (name : String, map : Map[String,Long]) = ((cont : Continuation) => {
      val tableID : TableID = new TableID(name)
      val row : Row = map.foldLeft (new Row()) ((acc,value) => acc.set(new RowID(value._1),value._2) )
      Schema.insert(tableID,row,x => cont(x))
    } ) : (Continuation => Unit)
    def selectStatement    (name : String) = ((cont : Continuation) => {
      val tableID  : TableID = new TableID(name)
      val handler  : Row => Unit = ( row : Row) => cont(row.data.foldLeft(Map[String,Long]()) ((acc,value) => acc +(value._1.id -> value._2.asInstanceOf[Long]))) 
      Schema.select(tableID,handler)
    }) : (Continuation => Unit)
    def updateStatement    (name : String, map : Map[String,Long]) = ((cont : Continuation) => {
      val tableID  : TableID = new TableID(name)
      def constantFunction(value : Any) = (_ : Row) => value
      def addUpdate(acc : Map[RowID,(Row => Any)],key : String,value : Any) = acc.updated(new RowID(""),"")
      val update   : Map[RowID,(Row => Any)] = map.foldLeft(Map[RowID,(Row => Any)]())((acc,value) => acc.updated(new RowID(value._1),constantFunction(value._2)))
      Schema.update(tableID,update)
      cont(success)
    }) : (Continuation => Unit)

    def deleteStatement    (name : String, map : Map[String,Long]) = ((cont : Continuation) => {
      val tableID  : TableID = new TableID(name)
      def test(key : String,mapStringLong : Map[String,Long],mapRow : Map[RowID,Any]) = {
	val rowID : RowID = new RowID(key)
	mapStringLong.contains(key) && mapRow.contains(rowID) && mapRow(rowID).equals(mapStringLong(key))
      }
      val filter   : (Row => Boolean) = (row : Row) => map.forall(x => test(x._1,map,row.data))
      Schema.delete(tableID,filter)
      cont(success)
    }) : (Continuation => Unit)
  }
  
  object BoboParser extends RegexParsers with RunParser {
    val ID        = """([a-zA-Z0-9]+)"""r
    def id :  Parser[String]  = ID ^^ (_.mkString)
    val NUM    = """([1-9][0-9]*)"""r
    def num : Parser[Long]    = ID ^^ (_.mkString.toLong)
    val INSERT    = "(?i)insert"r
    val DELETE    = "(?i)delete"r
    val SELECT    = "(?i)select"r
    val UPDATE    = "(?i)update"r 
    val WHERE     = "(?i)where"r
    val COLON     = ":"r
    val SEMI      = ";"r
    val COMMA     = ","r

    type RootType = (BoboHandler.Continuation => Unit)
    val entry     : Parser[Map[String,Long]]  = id ~ COLON ~ num ^^ { case id ~ _ ~ num => Map[String,Long](id -> num)  }
    val map       : Parser[Map[String,Long]]  = entry ~ (( COMMA ~> entry)*) ^^ { case entry ~ tail => tail.foldLeft(entry)((acc,value) => acc ++ value) }
    val insert    : Parser[RootType]= INSERT ~ id ~ map  ^^ { case _ ~ id ~ map  => BoboHandler.insertStatement(id,map) } 
    val delete    : Parser[RootType]= DELETE ~ ID ~ map  ^^ { case _ ~ id ~ map  => BoboHandler.deleteStatement(id,map) } 
    val select    : Parser[RootType]= SELECT ~ ID        ^^ { case _ ~ id        => BoboHandler.selectStatement(id) } 
    val update    : Parser[RootType]= UPDATE ~ ID ~ map  ^^ { case _ ~ id ~ map  => BoboHandler.updateStatement(id,map) } 
    val statement : Parser[RootType]= (insert | delete | select | update ) ^^ { case s => s }

    def root = statement
  }
}
