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
 * Annotation placed on a method parameter to
 * export a column to a temporal table. A column
 * will not be exported if this annotation is
 * not place.  It takes the parameter name of
 * column name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Column {
	String value();
}
