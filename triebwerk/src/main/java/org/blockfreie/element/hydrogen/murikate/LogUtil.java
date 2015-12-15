/**
 *  $filename$
 *  Class to create logger
 *  $Id: LogUtil.java 263 2011-01-08 16:48:32Z mwm1 $
 *
 *  Copyright (c) 2009 by Tom Maasha <maasha@blockfreie.org>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 *  02111-1307, USA.
 **/
package org.blockfreie.element.hydrogen.murikate;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
/**
 * LogUtil contains factory methods to construct loggers
 * to be used by a class.  They are attached to the namespace
 * of a class and should be declared static.
 * 
 * <code>
 * private static final Logger gLogger = LogUtil.createLogger();
 * </code>
 * 
 * @author Mutaamba Maasha
 */

final public class LogUtil 
{ static final String LOGGING_PROPERTIES="logging.properties";
  static final Logger LOGGER = Logger.getLogger(LogUtil.class.getCanonicalName());
  static 
  {  try
     {  InputStream inputStream = LogUtil.class.getResourceAsStream(LOGGING_PROPERTIES);
	    if(inputStream !=null)
	    {  LogManager.getLogManager().readConfiguration(inputStream); }
	}catch (Exception e)
	{  LOGGER.log(Level.SEVERE,"fail loading configuration",e); }
  }

  private LogUtil(){}

  /**
   * Returns an {@link Logger} with the correct namespace as the caller.
   * @param  level the {@link Level} of the logger
   * @return {@link Logger}
   */
  public static Logger createLogger(Level level)
  { Throwable throwable = new Throwable();
    String classname = throwable.getStackTrace()[2].getClassName();
    Logger result = Logger.getLogger(classname);
    if(level!=null)
    result.setLevel(level);
        return result;
  }
  /**
   * Returns an {@link Logger} with the correct namespace as the caller.
   * @return {@link Logger}
   */
  public static Logger createLogger() 
  {     return createLogger(null); }
}