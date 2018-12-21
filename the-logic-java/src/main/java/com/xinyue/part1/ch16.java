package com.xinyue.part1;
public class ch16 {
    public static void main(String[] args) {
        System.out.println(max(0));
        System.out.println(max(0,2));
        System.out.println(max(0,2,4));
        System.out.println(max(0,2,4,5));
    }

    public static int max(int min, int ... a) {
        int max = min;
        for(int i=0; i<a.length; i++) {
            if(max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }
}