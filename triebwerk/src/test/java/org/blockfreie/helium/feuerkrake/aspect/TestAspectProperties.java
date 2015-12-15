package org.blockfreie.helium.feuerkrake.aspect;


import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAspectProperties {
    private static IEmitterHandler HANDLER;

    @BeforeClass public static void setUp()
    { HANDLER = AspectEmitter.setHandler(new NullEmitterHandler()); }
    @AfterClass  public static void tearDown()
    { AspectEmitter.setHandler(HANDLER); }

    @Test
    public void insert()
    {   Integer a = new Random().nextInt();
        Integer b = new Random().nextInt();
        Assert.assertEquals(new Integer(a+b),new Arithmetic().add(a,b));
        
    }
    static public void main(String arg[]){
    	new TestAspectProperties().insert();
    }

}
