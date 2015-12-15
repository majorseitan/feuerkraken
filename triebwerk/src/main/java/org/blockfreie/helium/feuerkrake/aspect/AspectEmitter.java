/**
 * Krake is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 */
package org.blockfreie.helium.feuerkrake.aspect;
import static org.blockfreie.element.hydrogen.murikate.ApplicationUtil.getContext;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.blockfreie.element.hydrogen.murikate.LogUtil;
import org.blockfreie.helium.feuerkrake.annotation.Table;
/**
 * @author Mutaamba Maasha
 *
 */
@Aspect
public class AspectEmitter {
          static private Logger  LOGGER = LogUtil.createLogger(Level.SEVERE);
    //    static private Logger  LOGGER = LogUtil.createLogger();

        static private IEmitterHandler mHandler=(IEmitterHandler)getContext().getBean("emitterHandler");
        static public IEmitterHandler getHandler()
        {       LOGGER.log(Level.INFO,mHandler==null?"null":mHandler.toString());
                return mHandler; 
        }
        static public IEmitterHandler setHandler(IEmitterHandler handler)
        { IEmitterHandler tmp = mHandler;
          mHandler = handler;
          return tmp;
        }
        
    @Pointcut("execution (@org.blockfreie.helium.feuerkrake.annotation.Table * *(..)) && @annotation(table)")
    void callTableMethod(Table table){}

    @Around("callTableMethod(table)")
    public Object around(ProceedingJoinPoint joinpoint,Table table)
    throws Throwable
    { return getHandler().handle(table.value(),joinpoint); }
}
