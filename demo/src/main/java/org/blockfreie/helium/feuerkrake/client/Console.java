package org.blockfreie.helium.feuerkrake.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.blockfreie.element.hydrogen.murikate.ApplicationUtil;
import org.blockfreie.helium.feuerkrake.aspect.AspectEmitter;
import org.blockfreie.helium.feuerkrake.client.IHarness;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jline.ConsoleReader;

import scala.Tuple2;

public class Console {
    static final String APPLLICATION_PROMPT = ">";
    static final String APPLICATION_CONTEXT = "/ApplicationContext.xml";
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {APPLICATION_CONTEXT});
	final IHarness harness = context.getBean(IHarness.class);
	Class.forName(AspectEmitter.class.getCanonicalName());
	//		final IHarness harness = ApplicationUtil.getContext().getBean(IHarness.class);
	ConsoleReader reader = new ConsoleReader();

        if (reader != null) {
	    String sql = null;
	    while((sql = reader.readLine(APPLLICATION_PROMPT)) != null)
		{ try {
			harness.execute(sql);
			for(Object obj : harness.getMessages())
			    { 	if(scala.collection.immutable.Map.class.isInstance(obj))
				    { List<Object> buffer = new ArrayList<Object>();
					scala.collection.immutable.Map map = (scala.collection.immutable.Map)obj;
					scala.collection.Iterator iterator = map.iterator();
					if(map.keySet().contains("1"))
					    {
						while(iterator.hasNext())
						    { Tuple2<String,Object> tuple2 = (Tuple2<String,Object>)iterator.next();
							String key = tuple2._1();
							Integer index = Integer.parseInt(key)-1;
							buffer.add(index,tuple2._2());
						    }
						System.out.println(buffer);
					    }else{ System.out.println(obj); }
				    }
        	  		else
				    { System.out.println(obj); }
			    }
		    }catch(Exception e) { System.err.println(e); e.printStackTrace(); }
        	}
        }
    }
}
