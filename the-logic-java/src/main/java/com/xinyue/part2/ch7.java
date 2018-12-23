package com.xinyue.part2;

public class ch7 {
    public static void main(String[] args) {
        boolean b1 = false;
        Boolean bObj = Boolean.valueOf(b1);
        boolean b2 = bObj.booleanValue();

        int i1 = 12345;
        Integer iObj = Integer.valueOf(i1);
        int i2 = iObj.intValue(); 
        
        System.out.println(b1 + " " + i2);

        Boolean b = Boolean.valueOf("true");
        Float f = Float.valueOf("123.45");
    }
}