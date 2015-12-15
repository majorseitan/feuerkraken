package org.blockfreie.helium.feuerkrake.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.blockfreie.element.hydrogen.murikate.*;

/**
 * @author Mutaamba Maasha
 *
 */
public class NullEmitterHandler implements IEmitterHandler 
{ interface ITransform { public Object[] tranform(Object[] arg); } 
 
  private static final Logger LOGGER = LogUtil.createLogger(Level.ALL); 
  static public ITransform transform = new ITransform(){  public Object[] tranform(Object[] arg){ return arg; } };
  static public List<StackFrame> stackFrames = new ArrayList<StackFrame>();
  public Object handle(String name, ProceedingJoinPoint joinpoint) throws Throwable
  { StackFrame stackFrame = new StackFrame();
    stackFrame.name = name;
    stackFrame.arguments = joinpoint.getArgs();
    stackFrame.returnValue = joinpoint.proceed(transform.tranform(joinpoint.getArgs()));
    stackFrames.add(stackFrame);
    LOGGER.log(Level.INFO,stackFrame.toString());
    return stackFrame.returnValue;
  }
}
