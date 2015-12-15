/**
 *  $filename$
 *  Class to create java.util.Map
 *  $Id$
 *
 *  Copyright (c) 2008 by Mutaamba Maasha <maasha@blockfreie.org>
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

import java.util.HashMap;
import java.util.Map;

/**
 * MapUtil contains factory methods to construct maps.
 * <p>
 * <code>
 * Map&lt;Key,Value&gt; map = MapUtil.map();              // null constructor
 * Map&lt;Key,Value&gt; map = MapUtil.map(key,value);     // value constructor
 * Map&lt;Key,Value&gt; map = MapUtil.map(map,key,value); // recursive constructor
 * </code>
 * </p>
 * 
 * @author Mutaamba Maasha
 */

final public class MapUtil {
    private MapUtil() {
    }

    /**
     * Adds a entry to a map namely (key,value).
     * 
     * @param <Key>
     *            type of the key
     * @param <Value>
     *            type of the value
     * @param accumulator
     *            map to be added to
     * @param key
     *            to be placed as the single entry
     * @param value
     *            to be placed as the single entry
     * @return map of type Map&lt;Key,Value&gt; with value (key,value).
     */

    public static <Key, Value> Map<Key, Value> map(Map<Key, Value> accumulator,
            Key key, Value value) {
        accumulator.put(key, value);
        return accumulator;
    }

    /**
     * Constructs a new map with one entry namely (key,value).
     * 
     * @param <Key>
     *            type of the key
     * @param <Value>
     *            type of the value
     * @param key
     *            to be placed as the single entry
     * @param value
     *            to be placed as the single entry
     * @return map of type Map&lt;Key,Value&gt; with value (key,value).
     */
    public static <Key, Value> Map<Key, Value> map(Key key, Value value) {
        Map<Key, Value> result = map();
        result.put(key, value);
        return result;
    }

    /**
     * Constructs a new empty map. A hash map is used.
     * 
     * @param <Key>
     *            type of the key
     * @param <Value>
     *            type of the value
     * @return empty map of type Map&lt;Key,Value&gt
     */
    public static <Key, Value> Map<Key, Value> map() {
        Map<Key, Value> result = new HashMap<Key, Value>();
        return result;
    }
}
