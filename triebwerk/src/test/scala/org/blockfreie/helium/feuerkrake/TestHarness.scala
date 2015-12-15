import org.blockfreie.helium.feuerkrake._
package org.blockfreie.helium.feuerkrake{
  import org.junit.After;
  import org.junit.Before;
  import org.junit.Test;
  import junit.framework.Assert
  import org.blockfreie.helium.feuerkrake.client.IHarness
  class TestHarness{
    val random : scala.util.Random = new scala.util.Random

    @Test def testRegisterMethods = {
      val harness : Harness = new Harness()
      harness.registerMethods(classOf[org.blockfreie.helium.feuerkrake.SampleMethods])
      harness.execute("call id(1)")
      Assert.assertEquals(harness.messages,List(1L))
    }
    @Test def testRegisterClass = {
      val harness : Harness = new Harness()
      val value   : Long    = random.nextLong
      harness.registerTables(classOf[org.blockfreie.helium.feuerkrake.SampleClass])
      harness.query("select * from %s".format(org.blockfreie.helium.feuerkrake.SampleClass.DOUBLE_TABLE_NAME ))
      harness.execute("insert into %s (x) values (%d)".format(org.blockfreie.helium.feuerkrake.SampleClass.DOUBLE_TABLE_NAME,value))
      val messages : List[Any] = harness.messages
      val result : Long = messages(0).asInstanceOf[Long]
      val map   : java.util.Map[String,Any] = messages(1).asInstanceOf[java.util.Map[String,Any]]
      Assert.assertEquals(map.get("result"),result)
    }
  }
}
