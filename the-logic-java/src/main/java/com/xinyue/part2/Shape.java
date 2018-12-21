package com.xinyue.part2;
/**
 * 父类Shape, 表示图形
 */
public abstract class Shape {
    private static final String DEFAULT_COLOR = "black";
    private String color;

    public Shape() {
        this(DEFAULT_COLOR);
    }

    public Shape(String color) {
        this.color = color;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    public abstract void draw();
}