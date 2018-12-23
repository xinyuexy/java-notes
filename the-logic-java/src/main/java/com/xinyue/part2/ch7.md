## Part2 面向对象


### 第7章 常用基础类
#### 7.1 包装类
>包装类有什么用呢？Java中很多代码(比如后续文章介绍的集合类）只能操作对象，为了能操作基本类型，需要使用其对应的包装类，另外，包装类提供了很多有用的方法，可以方便对数据的操作。

* **基本类型和包装类的相互转换** <br>
  ```java
  boolean b1 = false;
  Boolean bObj = Boolean.valueOf(b1);
  boolean b2 = bObj.booleanValue();

  int i1 = 12345;
  Integer iObj = Integer.valueOf(i1);
  int i2 = iObj.intValue();
  ```
每种包装类都有一个静态方法`valueOf()`，接受基本类型，返回引用类型，也都有一个实例方法`xxxValue`()返回对应的基本类型。其他如double,float,char等类型也是类似.

* **自动拆箱和装箱**
  ```java
    Integer a = 100;
    int b = a;
  ```
  >自动装箱/拆箱是Java编译器提供的能力，背后，它会替换为调用对应的valueOf()/xxxValue()

  比如说，上面的代码会被Java编译器替换为：
  ```java
    Integer a = Integer.valueOf(100);
    int b = a.intValue();
  ```
* **包装类构造方法new**
  ```java
    Integer a = new Integer(100);
    Boolean b = new Boolean(true);
  ```
  >那到底应该用静态的valueOf方法，还是使用new呢？一般建议使用valueOf。new每次都会创建一个新对象

* **重写Object类方法** <br>
  所有包装类都重写了Object类的如下方法：
  ```java
  boolean equals(Object obj)
  int hashCode()
  String toString()
  ```

* **包装类和String** <br>
  1.除了Character外，每个包装类都有一个静态的valueOf(String)方法，根据字符串表示返回包装类对象:
  ```java
    Boolean b = Boolean.valueOf("true");
    Float f = Float.valueOf("123.45f"); 
  ```
  2.有一个静态的parseXXX(String)方法，根据字符串表示返回基本类型值
  ```java
    boolean b = Boolean.parseBoolean("true");
    double d = Double.parseDouble("123.45");
  ```
* **共同父类Number**
* **不可变性** <br>
  包装类都是不可变类，所谓不可变就是，实例对象一旦创建，就没有办法修改了。这是通过如下方式强制实现的：<br>
  1.所有包装类都声明为了final，不能被继承<br>
  2.内部基本类型值是私有的，且声明为了final<br>
  3.没有定义setter方法<br>

* **剖析Integer与二进制算法** <br>
  `reverse`位翻转 <br>
  `reverseBytes`字节翻转 <br>
  >内部实现原理，理解位运算

* **valueOf的实现** <br>
  IntegerCache, 共享常用对象：享元模式