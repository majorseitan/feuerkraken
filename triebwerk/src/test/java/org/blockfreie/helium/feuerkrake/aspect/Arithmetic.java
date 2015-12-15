package org.blockfreie.helium.feuerkrake.aspect;

import java.util.List;

import org.blockfreie.helium.feuerkrake.annotation.Column;
import org.blockfreie.helium.feuerkrake.annotation.Table;

/**
 * @author Mutaamba Maasha
 *
 */
public class Arithmetic 
{       public static final String ADD_TABLE_NAME = "965c30f0-1905-11e0-ac64-0800200c9a66";
        public static final String SUBTRACT_TABLE_NAME ="965c30f1-1905-11e0-ac64-0800200c9a66";
        public static final String PRODUCT_TABLE_NAME ="965c30f2-1905-11e0-ac64-0800200c9a66";

        @Table(ADD_TABLE_NAME)
        public Integer add(@Column("a") Integer a,@Column("b")Integer b){return a + b;}
        @Table(SUBTRACT_TABLE_NAME)
        public Integer substract(@Column("a") Integer a,@Column("b")Integer b){return a - b;}
        @Table(PRODUCT_TABLE_NAME)
        public Integer product(@Column("values") List<Integer> values)
        {       Integer result = 1;
        for(Integer value : values)
        { result *= value; }
        return result;
        }

        public static Integer ADD(Integer a,Integer b) { return new Arithmetic().add(a,b); }

}
