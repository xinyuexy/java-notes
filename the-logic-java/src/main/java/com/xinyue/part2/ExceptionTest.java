package com.xinyue.part2;

public class ExceptionTest {
    public static void main(String[] args) {
        try {
            String s = null;
            s.indexOf("a");
            System.out.println("end");
        } catch(NullPointerException e) {
            System.out.println("字符串为null");
            e.printStackTrace();
        }
    }
}