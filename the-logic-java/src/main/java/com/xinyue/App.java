package com.xinyue;
import com.xinyue.part2.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        //外部访问静态内部类
        Outer.StaticInner si = new Outer.StaticInner();
        si.innerMethod();
        //访问成员外部类
        Outer outer = new Outer();
        Outer.Inner in = outer.new Inner();
        in.innerMethod();
    }
}
