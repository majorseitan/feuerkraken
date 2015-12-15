import org.blockfreie.helium.feuerkrake._
package org.blockfreie.helium.feuerkrake{
  import scala.util.parsing.combinator._
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  import  org.blockfreie.helium.feuerkrake.annotation._;
  import org.blockfreie.helium.feuerkrake.client.IHarness
  import org.blockfreie.helium.feuerkrake.AQL._
  import java.lang.reflect._
  class NullHarness extends IHarness {
    def getMessages() : java.util.List[Object] = new java.util.ArrayList[Object]()
    def query(sql : String) = ()
    def execute(sql : String) = ()
    def registerMethods[T](clazz : Class[T]) = ()
    def registerTables[T](clazz : Class[T]) = ()
  }
}
