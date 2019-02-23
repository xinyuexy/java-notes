package com.xinyue.part3.ch8;

import java.util.ArrayList;
import java.util.List;

public class Plate<T> {
    private List<T> list;
    public Plate() {
        list = new ArrayList<>(); 
        System.out.println("Plate created");
    }
    public void add(T item) {list.add(item);}
    public T get() {return list.get(0);}

    public static void main(String[] args) {
        // Plate<Fruit> plate = new Plate<Fruit>();
        // plate.add(new Fruit());
        // plate.add(new Apple());
        // Plate<Fruit> plate2 = new Plate<Apple>();
        Plate<?> plate = new Plate<Fruit>();
    }
}