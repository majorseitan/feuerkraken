package org.blockfreie.helium.feuerkrake.aspect;

import java.util.Arrays;

/**
 * @author Mutaamba Maasha
 *
 */
public class StackFrame     {   public String   name;
public Object[] arguments;
public Object   returnValue;
public StackFrame(){}
/**
 * @param name
 * @param arguments
 * @param returnValue
 */
public StackFrame(String name, Object[] arguments, Object returnValue) {
        super();
        this.name = name;
        this.arguments = arguments;
        this.returnValue = returnValue;
}
/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(arguments);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
        + ((returnValue == null) ? 0 : returnValue.hashCode());
        return result;
}
/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        StackFrame other = (StackFrame) obj;
        if (!Arrays.equals(arguments, other.arguments)) return false;
        if (name == null) 
        {       if (other.name != null) return false; } 
        else if (!name.equals(other.name)) return false;
        if (returnValue == null) 
        {       if (other.returnValue != null) return false; } 
        else if (!returnValue.equals(other.returnValue)) return false;
        return true;
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
        return "StackFrame [name=" + name + ", arguments="
        + Arrays.toString(arguments) + ", returnValue="
        + returnValue + "]";
}

}
