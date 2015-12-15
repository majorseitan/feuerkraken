package org.blockfreie.helium.feuerkrake {
  import org.blockfreie.helium.feuerkrake._
  import org.blockfreie.helium.feuerkrake.annotation._
  import junit.framework.Assert
  import org.junit.Test;
  
  class  TestAspectContext
  { @Test def rowParametersAreMappedCorrectly = {
    AspectContext.register(classOf[org.blockfreie.helium.feuerkrake.test.TestA])  
    val addTableName : String = org.blockfreie.helium.feuerkrake.test.TestA.ADD_TABLE_NAME  
    val actual : List[Any] = AspectContext.rowToParametername(addTableName,new Row().set(new RowID("a"),1).set(new RowID("b"),2))
    val expected : List[Any] = List[Any](1,2)
    Assert.assertEquals(expected.length,actual.length)
    Assert.assertTrue(expected.zip(actual).forall(x => x._1 == x._2))
  }
 }
}
