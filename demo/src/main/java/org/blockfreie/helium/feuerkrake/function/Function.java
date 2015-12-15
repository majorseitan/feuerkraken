package org.blockfreie.helium.feuerkrake.function;

import org.blockfreie.helium.feuerkrake.annotation.Column;
import org.blockfreie.helium.feuerkrake.annotation.Export;
import org.blockfreie.helium.feuerkrake.annotation.Table;

public class Function {
	
	@Export("fibonacci")
	@Table("fibonacci")
	public static Long fibonacci(@Column("x")Long x)
	{	Long result = 1L;
		if(x > 2) { result=add(fibonacci(x-2),fibonacci(x-1)); 	}
		return result;
	}
	@Export("quit")
	public static void quit()
	{ System.exit(0); }
	@Export("identity")
	@Table("identity")
	 public static Object identity(@Column("x")Object x)                                                                                                                 
    { return x; }
	@Export("print")
	public static void print(@Column("x")Object x)
	{ System.out.println(x); }
	@Export("modulo2")
	public static Long modulo2(@Column("x")Long x)
	{ return (x%2L); }
	@Export("add")
	@Table("add")
	public static Long add(@Column("x")Long x,@Column("y")Long y)
	{ return (x+y); }
}
