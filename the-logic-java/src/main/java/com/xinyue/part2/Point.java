package com.xinyue.part2;
class Point implements MyComparable {
    private int x;
    private int y;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance() {
        return Math.sqrt(x*x+y*y);
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(x, p.getX()) + Math.pow(y, p.getY()));
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    @Override
    public int compareTo(Object other) {
        //先判断类型
        if(!(other instanceof Point)) {
            throw new IllegalArgumentException();
        }

        Point otherPoint = (Point)other;
        double delta = distance() - otherPoint.distance();
        if(delta < 0) {
            return -1;
        } else if(delta > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        // Point p = new Point(2, 3);
        // System.out.println(p.toString());
        MyComparable p1 = new Point(2, 3);
        MyComparable p2 = new Point(1, 2);
        System.out.println(p1.compareTo(p2));
    }
}