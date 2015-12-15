package org.blockfreie.helium.feuerkrake{

import org.aspectj.lang._
import org.aspectj.lang.annotation._
import org.blockfreie.helium.feuerkrake.aspect._
import java.util.logging.Logger
import java.util.logging.Level

  class ScalaAspectEmitter extends IEmitterHandler {
  import org.blockfreie.element.hydrogen.murikate.LogUtil
  import java.util.logging.Logger
  import java.util.logging.Level

  val LOGGER : Logger = LogUtil.createLogger(Level.INFO)
  def handle(name: String,joinpoint: ProceedingJoinPoint): Object = {
    val hanlder : (List[Any] => Any) = (argz :List[Any])=> {
	 val argzObject : Array[Object] = argz.map(x => x.asInstanceOf[Object]).toArray;
	 joinpoint.proceed(argzObject).asInstanceOf[Any]  
      }
      val args : List[Any] = joinpoint.getArgs.map(x => x.asInstanceOf[Any]).toList
      AspectContext.handle(name,joinpoint.getTarget.asInstanceOf[Any],args,hanlder).asInstanceOf[Object]
    }
  }
}
