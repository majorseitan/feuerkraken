/**
 * Krake is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 */
package org.blockfreie.helium.feuerkrake.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Mutaamba Maasha
 *
 */
public interface IEmitterHandler {
	public Object handle(String name,ProceedingJoinPoint joinpoint) throws Throwable;
}
