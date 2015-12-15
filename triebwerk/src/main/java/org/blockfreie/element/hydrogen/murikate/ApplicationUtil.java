/**
 *  $filename$
 *  Class to load application properties
 *  $Id: ApplicationUtil.java 263 2011-01-08 16:48:32Z mwm1 $
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ApplicationUtil is a helper class used to standardize properties across
 * applications. It expects the application properties to be in the root
 * directory in a file /application.properties Failure to load application will
 * be logged.
 * 
 * <code>
 * ApplicationUtil.getProperty("org.blockfreie.element.beryllium.common.Application.Constant");
 * </code>
 * 
 * @author Mutaamba Maasha
 */

/**
 * ApplicationUtil contains utilities
 * for application development.
 * 
 * Properties
 * in the resource root directory it will expect
 * /application.properties.
 * 
 * Message
 * message bundle will be in the resource root directory
 * /message.properties
 *
 *
 * @author Mutaamba Maasha
 */

final public class ApplicationUtil 
{   private ApplicationUtil() {};

    static private final Logger LOGGER = LogUtil.createLogger();
    static private final String APPLICATION_PROPERTY_FILE   = "/application.properties"; // default
    static private final String APPLICATION_MESSAGE_BUNDLE  = "message";
    static private final Properties PROPERTY = new Properties();

    static 
    { try {  PROPERTY.load(ApplicationUtil.class
                .getResourceAsStream(APPLICATION_PROPERTY_FILE));
          } catch (IOException e) 
        { LOGGER.log(Level.SEVERE, "Error loading property : "
              + APPLICATION_PROPERTY_FILE , e);
        }
    }
    static private String CONTEXT_FILE = "/beans.xml";
    static public String setContextFile(String  contextFile)
    { String result = CONTEXT_FILE; 
	CONTEXT_FILE=contextFile;
	return result;
    }
    static public ApplicationContext getContext(){ return (ApplicationContext)(new ClassPathXmlApplicationContext(CONTEXT_FILE)); }
	


    /**
     * Returns application wide property.
     * 
     * @param key
     *            the name of property to retrieve
     * @return the value of property
     */
    public static String getProperty(String key) {
	String result = null;
	String classname = new Throwable().getStackTrace()[1].getClassName();
	String saltedkey = String.format("%s.%s", classname, key);
	result = PROPERTY.getProperty(saltedkey);
	result = (result == null)?PROPERTY.getProperty(key):result;
	LOGGER.log(Level.INFO, String.format("%s:%s", saltedkey, result));
	return result;
    }


    /**
     * Returns application wide resource bundle.
     * 
     * @return the resource bundle
     */
    public static ResourceBundle messageBundle()
    { return ResourceBundle.getBundle(APPLICATION_MESSAGE_BUNDLE); }

    private static final Pattern EL_PATTERN = Pattern.compile("\\$\\{(\\w+):([^\\}]*)\\}");
    private static final String  EL_ENV_KEY="env";
    private static final String  EL_PROP_KEY="prop";

    /**
     * Returns a message bundle and uses the argument to format. the string
     * 
     * @param bundleKey
     *            key to retrive in the bundle
     * @param args
     *            to format into string
     * @return the formated resource bundle property
     */
    public static String messageFormat(String bundleKey, Object... args) {
	ResourceBundle bundle = messageBundle();
	String format = bundle.getString(bundleKey);
	String result = String.format(format, args);
	return elExpand(result);
    }

    /**
     * Expands expression of the form. ${evn:NAME} expands to the evniroment
     * variable $NAME ${prop:NAME} expands to the property $NAME
     * 
     * seen as a replacement of the java.text.MessageFormat
     * 
     * @param value
     *            to be expanded
     * @return the expanded string
     */
    public static String elExpand(String value) {
	StringBuffer buffer = new StringBuffer();
	Matcher matcher = EL_PATTERN.matcher(value);
	while (matcher.find()) {
	    String prelude = matcher.group(1);
	    String name = matcher.group(2);
	    String replacement;
	    if (EL_ENV_KEY.equals(prelude)) { replacement = System.getenv().get(name);
	    } else if (EL_PROP_KEY.equals(prelude)) { replacement = System.getProperty(name);
	    } else {
		replacement = matcher.group();
		LOGGER.log(Level.WARNING, String.format("Unknown expansion : '%s' : ignoring", replacement));
	    }
	    replacement = replacement.replaceAll("\\$", "\\\\\\$"); // 1. fix $
	    // bug with replacement - Illegal groupreference
	    matcher.appendReplacement(buffer, replacement);
	}
	matcher.appendTail(buffer);
	return buffer.toString();
    }

    public static Map<String, String> prefix() {
	Map<String, String> result = new HashMap<String, String>();
	String classname = new Throwable().getStackTrace()[1].getClassName();
	for (Map.Entry<Object, Object> entry : PROPERTY.entrySet()) {
	    if (entry.getKey().toString().startsWith(classname)) {
		String key = entry.getKey().toString();
		String value = entry.getValue().toString();
		LOGGER.log(Level.INFO,
			   String.format("{ key : %s , value : %s }", key, value));
		key = key.substring(classname.length() + 1);
		result.put(key, value);
	    }
	}
	return result;
    }
}
