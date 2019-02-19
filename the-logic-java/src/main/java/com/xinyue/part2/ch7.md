## Part2 面向对象


<!-- @import "[TOC]" {cmd="toc" depthFrom=1 depthTo=6 orderedList=false} -->

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

#### 7.2 剖析String
> Java中处理字符串的类主要是String和StringBuilder

* **基本用法** <br>
  通过常量定义String变量：<br>
  ```java
  String name = "ttee";
  ```
  通过new创建：
  ```java
  String name = "ttee";
  ```
  常用函数：`String`中提供了很多函数，熟悉常用函数的用法对我们日常的使用有很大帮助，具体可以参考JDK文档
  <img src="https://ws1.sinaimg.cn/mw690/006SQxbply1g0c0b5b6sij30s50f9jt5.jpg"/>

* **走进String内部** <br>
  String内部实现其实是由一个字符数组维护的，String中的绝大多数方法都是在操作这个字符数组。字符数组的定义如下：
  ```java
  private final char value[];
  ```
  如String的`length()`方法的源码如下：
  ```java
  public int length() {
      return value.length;
  }
  ```
  可以看到返回的是value的长度。<br>
  `chatAt`方法源码如下:
  ```java
  public char charAt(int index) {
      if ((index < 0) || (index >= value.length)) {
          throw new StringIndexOutOfBoundsException(index);
      }
      return value[index];
  }
  ```
  `substring()`方法源码如下：
  ```java
  public String substring(int beginIndex) {
      if (beginIndex < 0) {
          throw new StringIndexOutOfBoundsException(beginIndex);
      }
      int subLen = value.length - beginIndex;
      if (subLen < 0) {
          throw new StringIndexOutOfBoundsException(subLen);
      }
      return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
  }
  ```
  可以看到`substring`其实是计算好要截取的长度，然后调用String的构造函数重新创建了一个字符串。

  String有两个构造方法，可以根据char数组创建String:
  ```java
  public String(char value[])
  public String(char value[], int offset, int count)
  ```
  需要说明的是，String会根据参数新创建一个数组，并拷贝内容，而不会直接用参数中的字符数组，其内部实现代码如下：
  ```java
  public String(char value[]) {
      this.value = Arrays.copyOf(value, value.length);
  }
  ```
  `copyof`方法源码如下:
  ```java
  public static char[] copyOf(char[] original, int newLength) {
      char[] copy = new char[newLength];
      System.arraycopy(original, 0, copy, 0,
                        Math.min(original.length, newLength));
      return copy;
  }
  ```
  `this.value`即为String内部维护的实例变量字符数组，可以看到调用`copyof`方法会返回一个新创建的`char[]`数组，将其赋值给`this.value`.

* **不可变性** <br>
  > String类有个很重要的特性就是不可变性，即一旦被创建，就不能修改了，通过前面源码可以看出String类也声明为了final，不能被继承，内部char数组value也是final的，初始化后就不能再变了。

  这就可以解释为什么String类里面很多修改或查找的方法都是新建一个字符串了，如前面的`substring`，大都通过`Arrays.copyof()`方法复制新建。

* **常量字符串** <br>
  Java中的常量字符串就像一个String类型的对象一样，可以直接调用String的各种方法：
  ```java
  System.out.println("ttee".length());
  System.out.println("ttee".contains("tt"));
  System.out.println("ttee".indexOf("ee"));
  ```
  在内存中，它们被存放在字符串常量池中，每个常量只会保存一份，被所有使用者共享。
  我们可以看看下面的代码例子：
  ```java
  String name1 = "ttee";
  String name2 = "ttee";
  System.out.println(name1==name2);
  ```
  答案输出为true，因为name1和name2都指向的是字符串常量池中的同一个对象（即所存储的地址相同），再看下面的代码：
  ```java
  String name1 = new String("ttee");
  String name2 = new String("ttee");
  System.out.println(name1==name2);
  ```
  答案时false，这时会创建两个String对象，而name1和name2分别指向这两个对象，只不过这两个对象内部所存储的地址都指向相同的char数组。内存布局如下：
  <img src="https://ws1.sinaimg.cn/mw690/006SQxbply1g0c1gkm8cxj30iy0b475p.jpg"/>

* **hashcode** <br>
  除了value这个实例变量，String内部还会缓存hash实例变量：
  ```java
  private int hash; // Default to 0
  ```
  `hashcode()`方法会计算并返回这个值：
  ```java
  public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;

        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
  }
  ```
  如果缓存的hash不为0，就直接返回了，否则根据字符数组中的内容计算hash
  > 为什么要用这个计算方法呢？这个式子中，hash值与每个字符的值有关，每个位置乘以不同的值，hash值与每个字符的位置也有关。使用31大概是因为两个原因，一方面可以产生更分散的散列，即不同字符串hash值也一般不同，另一方面计算效率比较高

* **正则表达式** <br>

#### 7.3 剖析StringBuilder
* **基本用法**
  ```java
  StringBuilder sb = new StringBuilder();
  sb.append("tt");
  sb.append("ee");
  System.out.println(sb.toString());
  //out: ttee
  ```

* **实现原理** <br>
  与String类似，StringBuilder类也封装了一个字符数组，定义如下：
  ```java
  char[] value;
  ```
  与String不同，它不是final的，可以修改。另外，与String不同，字符数组中不一定所有位置都已经被使用，它有一个实例变量，表示数组中已经使用的字符个数，定义如下：
  ```java
  int count;
  ```
  append实现：
  ```java
  public AbstractStringBuilder append(String str) {
      if (str == null) str = "null";
      int len = str.length();
      ensureCapacityInternal(count + len);
      str.getChars(0, len, value, count);
      count += len;
      return this;
  }
  ```
  append会直接拷贝字符到内部的字符数组中，如果字符数组长度不够，会进行指数扩展.


参考：《Java编程的逻辑》 第7章 常用基础类