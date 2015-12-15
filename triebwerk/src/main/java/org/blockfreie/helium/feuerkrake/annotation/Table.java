/**
 * Krake is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 */
package org.blockfreie.helium.feuerkrake.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mutaamba Maasha
 *
 * Annotation placed on methods to
 * create a temporal table.  It takes
 * the required parameter of table name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Table {
	String value();
}
