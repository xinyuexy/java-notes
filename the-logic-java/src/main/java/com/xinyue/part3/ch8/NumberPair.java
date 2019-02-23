package com.xinyue.part3.ch8;

public class NumberPair<U extends Number, V extends Number> extends Pair<U, V> {
    public NumberPair(U first, V second) {
        super(first, second);
    }

    public double sum() {
        return getFirst().doubleValue() + getSecond().doubleValue();
    }

    public static void main(String[] args) {
        NumberPair<Integer,Double> pair = new NumberPair<>(10, 12.34);
        double sum = pair.sum();
        System.out.println(sum);
    }
}