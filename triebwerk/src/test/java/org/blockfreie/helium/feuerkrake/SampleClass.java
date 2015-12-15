package org.blockfreie.helium.feuerkrake;

import java.util.List;

import org.blockfreie.helium.feuerkrake.annotation.Column;
import org.blockfreie.helium.feuerkrake.annotation.Table;

public class SampleClass{
    public static final String   DOUBLE_TABLE_NAME    = "b965c30f0190511e0ac640800200c9b";
    @Table(DOUBLE_TABLE_NAME)
    static public Long doubleX(@Column("x")Long x)
    { return 2*x; }
}
