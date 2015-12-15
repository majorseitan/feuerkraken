package org.blockfreie.helium.feuerkrake.test;
import java.util.List;

import org.blockfreie.helium.feuerkrake.annotation.Column;
import org.blockfreie.helium.feuerkrake.annotation.Table;

public class TestA{
    public static final String ADD_TABLE_NAME      = "a965c30f0190511e0ac640800200c9a";
    public static final String SUBTRACT_TABLE_NAME = "a965c30f1190511e0ac640800200c9a";
    public static final String PRODUCT_TABLE_NAME  = "a965c30f2190511e0ac640800200c9a";

    @Table(ADD_TABLE_NAME)
    static public Long add(@Column("a") Long a,@Column("b")Long b)
    { return a+b;}
    @Table(SUBTRACT_TABLE_NAME)
    static public Long substract(@Column("a") Long a,@Column("b")Long b)
    {   return a-b; }
    @Table(PRODUCT_TABLE_NAME)
    public Integer product(@Column("values") List<Integer> values)
    {   Integer result = 1;
        for(Integer value : values)
        { result *= value; }
        return result;
    }
    public static Long wrapped(){ return add(1L,1L)*add(1L,2L); } 
}