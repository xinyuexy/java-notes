package com.xinyue.part3.ch8;

public class Pair<U, V> {
    U first;
    V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public static void main(String[] args) {
        Pair<String, Integer> pair = new Pair<>("xinyue", 100);
        String first = pair.getFirst();
        Integer second = pair.getSecond();
        System.out.println("min: " + first);
        System.out.println("max: " + second);
    }
}