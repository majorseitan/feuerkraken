import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

package org.blockfreie.helium.feuerkrake{
  class TestSchema{
    def random  : scala.util.Random = new scala.util.Random();
    def getRows() : List[Row] = { (1 to 9).map(x => new Row(Map(new RowID("test") -> random.nextInt()))).toList }
    def getTableID() : TableID = { new TableID(java.util.UUID.randomUUID.toString); }

    @Test
    def updateModifiesRows
    {   // update
      var store   : List[Row] = List[Row]();
      val tableID : TableID = getTableID;
      Schema.table(tableID, x => { store = store:::List(x); } )
      val rows    : List[Row] = getRows;
      val incrementTestRow = (row : Row) => row.data(new RowID("test")).asInstanceOf[Int] + 1
      Schema.update(tableID, Map(new RowID("test") -> incrementTestRow))
      rows.foreach( x => Schema.row(tableID,(x => x),x));
      val updatedRow : List[Row] = rows.map(x => new Row(Map(new RowID("test") ->  (x.data(new RowID("test")).asInstanceOf[Int] + 1)  )))
      Assert.assertTrue(store.zip(updatedRow).forall( x => x._1.equals(x._2)));
    }

    @Test
    def insertCallsMethod
    { var store : List[Row] = List[Row]();
      val tableID : TableID = getTableID;
      Schema.table(tableID, x => { store = store:::List(x); } )
      val rows    : List[Row] = getRows;
      rows.foreach( x => Schema.insert(tableID,x,x=>()));
      Assert.assertTrue(store.zip(rows).forall( x => x._1.equals(x._2)));
    }

    @Test
    def deleteFiltersRows{
      var store   : List[Row] = List[Row]();
      val tableID : TableID = getTableID;
      Schema.table(tableID, x => { store = store:::List(x); } )
      val rows    : List[Row] = getRows;
      val oddOnly = (row : Row) => (row.data(new RowID("test")).asInstanceOf[Int] % 2)  == 1 : Boolean
      Schema.delete(tableID,oddOnly)
      rows.foreach( x => Schema.row(tableID,(x => x),x));
      Assert.assertTrue(store.zip(rows.filter((row : Row) => (row.data(new RowID("test")).asInstanceOf[Int] % 2)  == 0 )).forall( x => x._1.equals(x._2)));
    }
    @Test
    def canExecute{
      var expected : Int = random.nextInt();
      var store    : Int = 1;
      Schema.execute(() => expected : Int,(x : Int)=> { store = x}  )
      Assert.assertEquals(expected,store);
    }
    @Test
    def selectRecievesrows{
      var store   : List[Row] = List[Row]();
      var keep  = (row  : Row) => store = store:::List(row)
      val tableID : TableID = getTableID;
      val rows    : List[Row] = getRows;
      Schema.table(tableID, x => x )
      Schema.select(tableID,keep);
      rows.foreach( x => Schema.row(tableID,(x => x),x));
      Assert.assertTrue(store.zip(rows).forall( x => x._1.equals(x._2)));
    }
    @Test
    def rowCallsFunction(){
      var store   : List[Row] = List[Row]();
      var keep  = (row  : Row) => { store = store:::List(row) ; row }
      val tableID : TableID = getTableID;
      val rows    : List[Row] = getRows;
      Schema.table(tableID, x => x )
      rows.foreach( x => Schema.row(tableID,keep,x));
      Assert.assertTrue(store.zip(rows).forall( x => x._1.equals(x._2)))
    }
    @Test
    def canCreateTable(){
      val tableID : TableID = getTableID;
      Schema.table(tableID, x => x );
    }
  }
}




/*

//execute
def execute(expression : (Any =>  Unit)) = { expression() }
Schema.catalog(tableID).update(new RowID("test"))(new Row(Map(new RowID("test") -> random.nextInt())))
val updatedRow : List[Row] = rows.map(x => new Row(Map(new RowID("test") ->  (x.data(new RowID("test")).asInstanceOf[Int] + 1)  )))
store.zip(updatedRow).forall( x => x._1.equals(x._2))




rows.zip(rows).forall( x => x._1.equals(x._2))
rows.foreach(x => Console.println(x.data))
store.foreach(x => Console.println(x.data))

Schema.row(new TableID("1"),new Row(Map[RowID,Any]()));
  Schema.insert(new TableID("1"),new Row(Map[RowID,Any]()));
  Schema.row(new TableID("2"),new Row(Map[RowID,Any]()));


updatedRow.foreach(x => Console.println(x.data))
store.foreach(x => Console.println(x.data))
store.zip(rows).forall( x => x._1.equals(x._2 + 1))
Schema.catalog(tableID).update(new RowID("test"))(new Row(Map(new RowID("test") -> random.nextInt())))
*/
