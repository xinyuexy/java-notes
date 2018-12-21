## Part2 面向对象

### 第4章 类的继承
> 使用继承一方面可以复用代码，公共的属性和行为可以放到父类中，而子类只需要关注子类特有的就可以了；另一方面，不同子类的对象可以更为方便地被统一处理。
#### 4.1 基本概念
* **根父类Object**
  默认`toString`方法:
    ```java
    Point p = new Point(2, 3);
    System.out.println(p.toString());
    //out: com.xinyue.part2.Point@15db9742
    ```
    可以看到`toString`方法的默认实现输出为该对象的全限定类名和地址
    Object中实现代码如下:
    ```java
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    ```
    重写`toString`方法
    ```java
    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
    //out: (2,3)
    ```
* **图形继承体系(代码实现)**
    1.父类`Shape`
    ```java
    package com.xinyue.part2;
    /**
     * 父类Shape, 表示图形
     */
    public class Shape {
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

        public void draw() {
            System.out.println("draw shape");
        }
    }
    ```
    2.子类`Circle`
    ```java
    package com.xinyue.part2;
    /**
    * 子类Circle, 继承Shape
    */
    public class Circle extends Shape {
        // 子类额外属性
        //中心点
        private Point center;
        //半径
        private double r;

        public Circle(Point center, double r) {
            this.center = center;
            this.r = r;
        }

        @Override
        public void draw() {
            System.out.println("draw circle at "
                    +center.toString()+" with r "+r
                    +", using color : "+getColor());   
        }

        public double area() {
            return Math.PI * r * r;
        }

        public static void main(String[] args) {
            Point center = new Point(2,3);
            Circle circle = new Circle(center,2);
            circle.draw();
            System.out.println(circle.area());
        }
    }
    ```
    程序输出:
    ```
    draw circle at (2,3) with r 2.0, using color : black
    12.566370614359172
    ```
    说明:
    1)Java不允许多继承
    2)子类不能直接访问父类的私有属性和方法
    **注意**:
    上述在调用子类Circle的构造函数时，会先调用父类的构造函数，由于没有显示指定，所以编译器会调用默认的，即设置颜色为"black", 我们也可以显示调用父类的构造函数，代码如下所示:
    ```java
    public Circle(Point center, double r) {
        super("red");
        this.center = center;
        this.r = r;
    }
    ```
    设置颜色"red"，注意一定要放在第一行.
    3.子类`Line`
    ```java
    package com.xinyue.part2;
    public class Line extends Shape {
        private Point start;
        private Point end;
        
        public Line(Point start, Point end, String color) {
            super(color);
            this.start = start;
            this.end = end;
        }

        public double length(){
            return start.distance(end);
        }
        
        public Point getStart() {
            return start;
        }

        public Point getEnd() {
            return end;
        }
        
        @Override
        public void draw() {
            System.out.println("draw line from "
                    + start.toString()+" to "+end.toString()
                    + ",using color "+super.getColor());
        }
    }
    ```
    4.带箭头直线`ArrowLine`
    ```java
    package com.xinyue.part2;
    public class ArrowLine extends Line {
        
        private boolean startArrow;
        private boolean endArrow;
        
        public ArrowLine(Point start, Point end, String color, 
                boolean startArrow, boolean endArrow) {
            super(start, end, color);
            this.startArrow = startArrow;
            this.endArrow = endArrow;
        }

        @Override
        public void draw() {
            super.draw();
            if(startArrow){
                System.out.println("draw start arrow");
            }
            if(endArrow){
                System.out.println("draw end arrow");
            }
        }
    }
    ```
    > super关键字用于指代父类，除了可以调用父类构造函数外，还可以访问父类非私有的成员变量和方法，当子类和父类没有同名方法时，可以不加super, 有的话，调用父类方法必须加super消除歧义
    
    5.图形管理器(统一处理)
    ```java
    package com.xinyue.part2;
    /**
     * 图形管理器类:统一处理不同子类型的对象
     */
    public class ShapeManager {
        private static final int MAX_NUM = 100;
        private Shape[] shapes = new Shape[MAX_NUM];
        private int shapeNum = 0;

        //不需要关心具体add哪个图形类型
        public void addShape(Shape shape) {
            if(shapeNum < MAX_NUM) {
                shapes[shapeNum++] = shape;
            }
        }

        public void draw() {
            for(int i=0; i<shapeNum; i++) {
                shapes[i].draw();
            }
        }

        public static void main(String[] args) {
            ShapeManager manager = new ShapeManager();
            manager.addShape(new Circle(new Point(4,4),3));
            manager.addShape(new Line(new Point(2,3),
                new Point(3,4),"green"));
            manager.addShape(new ArrowLine(new Point(1,2), 
                new Point(5,5),"black",false,true));
        
            manager.draw();
        }
    }
    ```
    > 可以说，多态和动态绑定是计算机程序的一种重要思维方式，使得操作对象的程序不需要关注对象的实际类型，从而可以统一处理不同对象，但又能实现每个对象的特有行为。

#### 4.2 继承的细节
* 构造方法
  当父类没有默认构造方法时，子类必须显示调用父类构造方法
* **构造方法调用重写方法**
  如果在父类构造方法中调用了可被重写的方法，则可能会出现意想不到的结果
  基类代码:
  ```java
    public class Base {
        public Base(){
            test();
        }
        
        public void test(){
        }
    }
  ```
  其中构造方法调用了重写方法`test()`, 子类代码:
  ```java
    public class Child extends Base {
        private int a = 123;
        
        public Child(){
        }
        
        public void test(){
            System.out.println(a);
        }
    }
  ```
  子类重写了父类方法`test()`, 并打印输出子类实例变量a
  ```java
  public static void main(String[] args){
    Child c = new Child();
    c.test();
  }
  ```
  输出结果是
  ```
  0
  123
  ```
  为什么打印了两次，还有第一行为什么是0呢？第一次输出是因为在new创建子类对象的过程中，会先调用父类构造方法, 父类构造方法进而调用子类重写的`test()`, 子类访问子类实例变量a, 注意这时a的赋值语句还没有执行,所以输出默认值0.

* **重名与静态绑定**
  > 子类可以重写父类非private的方法，当调用的时候，会动态绑定，执行子类的方法。那实例变量、静态方法、和静态变量呢？它们可以重名吗？如果重名，访问的是哪一个呢？
  **1.对于private变量和方法，它们只能在类内被访问，访问的也永远是当前类的
  2.在类外，则要看访问变量的静态类型，静态类型是父类，则访问父类的变量和方法，静态类型是子类，则访问的是子类的变量和方法**

  **动态绑定: 非私有方法
  静态绑定: 实例变量、静态变量、静态方法、private方法**

  (静态绑定：看声明时候的类型，不是new的类型)
<br/>
* **重载与重写**
  > 当有多个重名函数的时候，在决定要调用哪个函数的过程中，首先是按照参数类型进行匹配的，换句话说，寻找在所有重载版本中最匹配的，然后才看变量的动态类型，进行动态绑定。
* **父子类型转换**
  向上转型和向下转型
  > 一个父类的变量，能不能转换为一个子类的变量，取决于这个父类变量的动态类型（即引用的对象类型）是不是这个子类或这个子类的子类。

  判断父类变量是不是某个子类的对象
  ```java
  public boolean canCast(Base b){
    return b instanceof Child;
  }
  ```
* **protected**
  同一个包中的其他类访问
  子类访问
  设计模式: 模板方法

* **可见性重写**
  > 重写时，子类方法不能降低父类方法的可见性，不能降低是指，父类如果是public，则子类也必须是public，父类如果是protected，子类可以是protected，也可以是public，即子类可以升级父类方法的可见性但不能降低

#### 4.3 继承实现的基本原理
**认真分析书中这一节例子, 分析输出结果为什么是这样，涉及到了很多过程
(目前这一节还不是很能消化，后面反复再看)**
* 类的加载
* 创建对象
* 方法调用
* 变量访问

#### 4.4 为什么说继承是把双刃剑
* 继承破坏封装
* 继承没有反映is-a关系
* 如何应对继承的双面性
  1)使用final避免继承
  2)优先使用组合而非继承
  3)正确使用继承

参考: 《Java编程的逻辑》第四章-类的继承