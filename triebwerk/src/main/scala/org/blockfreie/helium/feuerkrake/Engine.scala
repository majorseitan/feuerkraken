package org.blockfreie.helium.feuerkrake{
  class RowID(val id : String) {
    override def toString = "{ 'class' : RowID , 'id' : %s } " format (id)
    override def hashCode = id.hashCode
    override def equals(other: Any) = other match {
      case that: RowID => (this.id == that.id)
      case _ => false
    }
  };
  class TableID(val id : String){
    override def toString = "{ 'class' : TableID , 'id' : %s } " format (id)
    override def hashCode = id.hashCode
    override def equals(other: Any) = other match {
      case that: TableID => (this.id == that.id)
      case _ => false
    }
  }
  class Row(val data : Map[RowID,Any]){
    def this() = this(Map[RowID,Any]())
    def set(rowID : RowID,any : Any) : Row = new Row(data.updated(rowID,any))
    def merge(row : Row) : Row = row.data.foldLeft(new Row(this.data))((acc,value)=>acc.set(value._1,value._2))
  }
  class TableInfo(var update :  Map[RowID,(Row => Any)]
                 ,var method : Row => Any
                 ,var delete : Row => Boolean
                 ,var select : List[Row => Unit] );
  object Schema {
  import scala.util.parsing.combinator._
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  import java.util.logging.Logger
  import java.util.logging.Level

  val LOGGER : Logger = LogUtil.createLogger(Level.INFO)

    var catalog = Map[TableID,TableInfo]();
    def execute[A](method : () => A ,handler : A => Unit ) = handler(method()) : Unit
    def update(table : TableID,update : Map[RowID,(Row => Any)]  ) = { update.foreach(x => ( catalog(table).update=catalog(table).update.updated(x._1,x._2) )) } : Unit
    def insert(table : TableID,row : Row,handler : Any  => Unit) = { handler(catalog(table).method(row)) } : Unit
    def delete(table : TableID,expression : (Row => Boolean)) = { catalog(table).delete=expression } : Unit
    def select(table:TableID,handler : Row => Unit) = {     catalog(table).select = handler::catalog(table).select  } : Unit
    def row(table: TableID,method : Row => Row,row : Row) : Option[Row] = { var copy : Row = row; catalog(table).update.foreach(x => copy = copy.set(x._1,x._2(row)));    if(catalog(table).delete(copy)){None} else { copy=method(copy);  catalog(table).select.foreach(x => x(copy)); Some(copy)} }
    def table(table: TableID,method : Row => Any) = { catalog=catalog.updated(table,new TableInfo(Map[RowID,(Row => Any)](),method,(row : Row)=> false,List[Row => Unit]())); }
  }
}
