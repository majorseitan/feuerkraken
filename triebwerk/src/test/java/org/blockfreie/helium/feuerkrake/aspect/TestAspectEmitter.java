package org.blockfreie.helium.feuerkrake.aspect;

/**
 * @author Mutaamba Maasha
 *
 */
import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;

 import junit.framework.Assert;

 import org.blockfreie.element.hydrogen.murikate.*;
import org.blockfreie.helium.feuerkrake.aspect.NullEmitterHandler.ITransform;
 import org.junit.AfterClass;
 import org.junit.BeforeClass;
import org.junit.Test;

public class TestAspectEmitter  {
//TODO        private static final Logger LOGGER = LogUtil.createLogger(Level.ALL);
    private static final Logger LOGGER = Logger.getLogger("org.blockfreie.helium.feuerkrake.aspect.TestAspectEmitter");
        private static IEmitterHandler HANDLER;

        @BeforeClass public static void setUp()
        { HANDLER = AspectEmitter.setHandler(new NullEmitterHandler()); }
        @AfterClass  public static void tearDown()
        { AspectEmitter.setHandler(HANDLER); }
            
    final private static Integer MAX=100;
        
    @Test
    public void hasSaneHandler()
    {   LOGGER.log(Level.INFO,"hasSaneHandler");
        Assert.assertEquals(NullEmitterHandler.class,AspectEmitter.getHandler().getClass());
    }
    
    @Test
    public void parametersArePreserved()
    {   LOGGER.log(Level.INFO,"parametersArePreserved");
                Integer a = new Random().nextInt();
                Integer b = new Random().nextInt();
                Assert.assertEquals(new Integer(a+b),new Arithmetic().add(a,b));
                Assert.assertEquals(new Integer(a-b),new Arithmetic().substract(a,b));
                Integer product = 1;
                List<Integer> values = new ArrayList<Integer>();
                for(int i =0;i < MAX;i++)
            {   int value = new Random().nextInt();
                        values.add(value);
                        product *= value;
            }
                Assert.assertEquals(product,new Arithmetic().product(values));
    }

    @Test
    public void methodCallsAreIntercepted()
    {   LOGGER.log(Level.INFO,"methodCallsAreIntercepted");
                Integer a = new Random().nextInt();
                Integer b = new Random().nextInt();
                Integer c = a+b;
                StackFrame expected = new StackFrame(Arithmetic.ADD_TABLE_NAME,new Object[]{a,b},c);
                Assert.assertEquals(new Integer(c),new Arithmetic().add(a,b));
                List<StackFrame> stackFrames = NullEmitterHandler.stackFrames;
                LOGGER.log(Level.INFO,Arrays.deepToString(stackFrames.toArray(new StackFrame[]{})));
                Assert.assertNotNull("missing null emitter",stackFrames);
                Assert.assertFalse("stack frames are empty",stackFrames.isEmpty());
                Assert.assertEquals(expected,stackFrames.get(stackFrames.size() -1));
    }
    
    @Test
    public void methodArgumentsCanBeTranformed()
    {   LOGGER.log(Level.INFO,"methodCallsAreIntercepted");
                Integer a = new Random().nextInt();
                Integer b = new Random().nextInt();
                Integer c = a+b;
                ITransform transform = NullEmitterHandler.transform;
                NullEmitterHandler.transform = new ITransform(){  public Object[] tranform(Object[] arg)
                	{ for(int i=0;i <arg.length;i++)
                	  {	arg[i]=(Integer)arg[i]+1; }
                	  return arg;
                	} 
                };
                Integer actual   = new Arithmetic().add(a,b);
                Integer expected = new Integer(c+2);
                Assert.assertEquals(expected,actual);
                NullEmitterHandler.transform=transform;

    }
    
    public static void main(String arg[]){
    	new TestAspectEmitter().methodArgumentsCanBeTranformed();
    }
}
