package org.blockfreie.helium.feuerkrake;

import  org.blockfreie.helium.feuerkrake.annotation.*;
public class SampleMethods { 
	static public @Export("id") Object id(Object a){ return a; } 
    @Table("identity")
    @Export("identity")
    static public Object identity(@Column("x")Object x)                                                                                                   
    { return x; }                                                                                                                                           
	
}
  