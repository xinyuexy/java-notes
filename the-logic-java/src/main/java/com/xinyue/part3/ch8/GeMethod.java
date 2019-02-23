package com.xinyue.part3.ch8;

//泛型方法测试
public class GeMethod {
    public static <T> int indexOf(T[] arr, T elm) {
        for(int i=0; i<arr.length; i++) {
            if(arr[i].equals(elm)) {
                return i;
            }
        }
        return -1;
    }

    public static <U,V> Pair<U,V> makePair(U first, V second){
        Pair<U,V> pair = new Pair<>(first, second);
        return pair;
    }

    public static void main(String[] args) {
        int index = indexOf(new Integer[]{1,3,5}, 3);
        System.out.println(index);
    }
}