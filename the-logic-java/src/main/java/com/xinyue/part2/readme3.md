## Part2 面向对象

### 第5章 类的扩展
#### 5.1 接口的本质
> 很多时候,我们实际上关心的,并不是对象的类型而是对象的能力,只要能够提供这个能力，类型并不重要
> 接口声明了一组能力，但它自己并没有实现这个能力，它只是一个约定，它涉及交互两方对象，一方需要实现这个接口，另一方使用这个接口，但双方对象并不直接互相依赖，它们只是通过接口间接交互

* **定义接口**
  ```java
  public interface MyComparable {
    int compareTo(Object other);
  }
  ```
  很多对象都可以比较，对于求最大值、求最小值、排序的程序而言，它们其实并不关心对象的类型是什么，只要对象可以比较就可以了，或者说，它们关心的是对象有没有可比较的能力。Java API中提供了Comparable接口，以表示可比较的能力

  >接口方法不需要加修饰符，加与不加都是public的，不能是别的修饰符

* **实现接口**
  在`Point`类中实现该接口
    ```java
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
    ```
  **注意实现接口必须实现该接口中的所有接口方法(Java8以后接口可以有默认方法)**
  一个类可以实现多个接口，表明类的对象具备多种能力

* **使用接口**
  > 与类不同，接口不能new，不能直接创建一个接口对象，对象只能通过类来创建。但可以声明接口类型的变量，引用实现了接口的类对象

  ```java
  public static void main(String[] args) {
        MyComparable p1 = new Point(2, 3);
        MyComparable p2 = new Point(1, 2);
        System.out.println(p1.compareTo(p2));
  }
  ```
  p1和p2可以调用MyComparable接口的方法，也只能调用MyComparable接口的方法，实际执行时，执行的是具体实现类的代码。

  为什么Point类型的对象非要赋值给MyComparable类型的变量呢？在以上代码中，确实没必要。但在一些程序中，代码并不知道具体的类型，这才是接口发挥威力的地方：
  ```java
    package com.xinyue.part2;
    import java.util.Arrays;
    public class CompUtil {
    public static Object max(MyComparable[] objs) {
        if(objs==null || objs.length == 0) {
            return null;
        }

        MyComparable max = objs[0];
        for(int i=1; i<objs.length; i++) {
            if(max.compareTo(objs[i]) < 0) {
                max = objs[i];
            }
        }

        return max;
    }

    public static void sort(MyComparable[] objs) {
        for(int i=0;i<objs.length;i++){
            int min = i;
            for(int j=i+1;j<objs.length;j++){
                if(objs[j].compareTo(objs[min])<0){
                    min = j;
                }
            }
            if(min!=i){
                 MyComparable temp = objs[i];
                 objs[i] = objs[min];
                 objs[min] = temp;
            }
        }
    }

    public static void main(String[] args) {
        Point[] points = new Point[]{
            new Point(2,3),
            new Point(3,4),
            new Point(1,2)
        };

        System.out.println("max: " + CompUtil.max(points));
        CompUtil.sort(points);
        System.out.println("sort: "+ Arrays.toString(points));
    }
  }
  ```
  可以看出，这个类是针对MyComparable接口编程，它并不知道具体的类型是什么，也并不关心，但却可以对任意实现了MyComparable接口的类型进行操作
  ```java
    Point[] points = new Point[]{
        new Point(2,3),
        new Point(3,4),
        new Point(1,2)
    };
    System.out.println("max: " + CompUtil.max(points));
    CompUtil.sort(points);
    System.out.println("sort: "+ Arrays.toString(points));
  ```
  输出如下：
  ```
  max: (3,4)
  sort: [(1,2), (2,3), (3,4)]
  ```
* **接口的细节**
  1.接口中的变量
  ```java
  public interface Interface1 {
    public static final int a = 0;
  }
  ```
  默认`public static final`，访问`Interface1.a`

  2.接口的继承
  ```java
  public interface IBase1 {
    void method1();
  }

  public interface IBase2 {
    void method2();
  }

  public interface IChild extends IBase1, IBase2 {
  }
  ```
  可以继承多个接口

* 使用组合和接口替代继承
  继承：1）复用代码；2）动态绑定，统一处理不同类型对象
  组合：复用代码
  接口：统一处理不同类型对象
  **组合+接口**

> 针对接口编程是一种重要的程序思维方式，这种方式不仅可以复用代码，还可以降低耦合，提高灵活性，是分解复杂问题的一种重要工具

#### 5.2 抽象类
> 抽象类就是抽象的类，抽象是相对于具体而言的，一般而言，具体类有直接对应的对象，而抽象类没有，它表达的是抽象概念

* **抽象方法和抽象类**
  ```java
  public abstract class Shape {
    // ... 其他代码
    public abstract void draw();
  }
  ```
  定义了抽象方法的类必须被声明为抽象类，不过，抽象类可以没有抽象方法

* **为什么需要抽象类**
  > 使用抽象方法，而非空方法体，子类就知道他必须要实现该方法，而不可能忽略。
使用抽象类，类的使用者创建对象的时候，就知道他必须要使用某个具体子类，而不可能误用不完整的父类

* **抽象类和接口**
  > 抽象类和接口是配合而非替代关系，它们经常一起使用，接口声明能力，抽象类提供默认实现，实现全部或部分方法，一个接口经常有一个对应的抽象类。

  比如说，在Java类库中，有：
  * Collection接口和对应的AbstractCollection抽象类
  * List接口和对应的AbstractList抽象类
  * Map接口和对应的AbstractMap抽象类
  
#### 5.3 内部类的本质
* **为什么需要内部类**
  1.内部类与包含它的外部类有比较密切的关系，而与其他类关系不大，定义在类内部，可以实现对外部完全隐藏，可以有更好的封装性，代码实现上也往往更为简洁。
  2.内部类可以方便的访问外部类的私有变量，可以声明为private从而实现对外完全隐藏，相关代码写在一起，写法也更为简洁.
  >不过，内部类只是Java编译器的概念，对于Java虚拟机而言，它是不知道内部类这回事的, 每个内部类最后都会被编译为一个独立的类，生成一个独立的字节码文件。
* **四种内部类**
  * 静态内部类
  * 成员内部类
  * 方法内部类
  * 匿名内部类
* **静态内部类**
  ```java
  public class Outer {
    private static int shared = 100;
    
    public static class StaticInner {
        public void innerMethod(){
            System.out.println("inner " + shared);
        }
    }
    
    public void test(){
        StaticInner si = new StaticInner();
        si.innerMethod();
    }
  }
  ```
  与外部类联系不大,可以访问外部类的静态变量和方法,但不可以访问实例变量和方法，在类内部，可以直接使用内部静态类，如test()方法所示。
  public静态内部类可以被外部使用，只是需要通过"外部类.静态内部类"的方式使用，如下所示：
  ```java
  Outer.StaticInner si = new Outer.StaticInner();
  si.innerMethod();
  ```

* 成员内部类
  ```java
  public class Outer {
    private int a = 100;
    
    //成员内部类
    public class Inner {
        public void innerMethod() {
            //直接访问外部类私有变量
            System.out.println("outer a " +a);
            //通过Outer.this访问
            Outer.this.action();
            //没有歧义的情况下可以不加Outer.this
            action();
        }
    }
    
    private void action(){
        System.out.println("action");
    }
    
    public void test(){
        Inner inner = new Inner();
        inner.innerMethod();
    }
  }
  ```
  成员内部类还可以直接访问外部类的实例变量和方法
  可以通过`Outer.this.`访问外部类实例变量和方法,一般在重名的情况下使用
  与静态内部类不同，成员内部类中不可以定义静态变量和方法
  >如果内部类与外部类关系密切，且操作或依赖外部类实例变量和方法，则可以考虑定义为成员内部类。

  在外部访问成员内部类:
  ```java
  //访问成员外部类
  Outer outer = new Outer();
  Outer.Inner in = outer.new Inner();
  in.innerMethod();
  ```
* 方法内部类
  1.方法内部类只能在定义的方法内使用
  2.可以访问外部类变量和方法(包括私有)

* 匿名内部类
  ```java
  public class Outer {
    public void test(final int x, final int y){
        Point p = new Point(2,3){                
                                               
            @Override                              
            public double distance() {             
                return distance(new Point(x,y));     
            }                                      
        };                                       
                                                 
        System.out.println(p.distance());        
    }
  }
  ```

>将程序分为保持不变的主体框架，和针对具体情况的可变逻辑，通过回调的方式进行协作，是计算机程序的一种常用实践。匿名内部类是实现回调接口的一种简便方式。
