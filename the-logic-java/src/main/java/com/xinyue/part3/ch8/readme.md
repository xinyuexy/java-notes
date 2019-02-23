## Part3 泛型与容器

### 第8章 泛型
#### 8.1 基本概念和原理
* **1.什么是泛型** <br>
>泛型将接口的概念进一步延伸，"泛型"字面意思就是广泛的类型，类、接口和方法代码可以应用于非常广泛的类型，代码与它们能够操作的数据类型不再绑定在一起，同一套代码，可以用于多种数据类型，这样，不仅可以复用代码，降低耦合，同时，还可以提高代码的可读性和安全性。

* **2.简单的泛型类** <br>
  ```java
    public class Pair<T> {
        T first;
        T second;

        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public T getSecond() {
            return second;
        }
    }
  ```
  在上述中T表示类型参数，泛型就是类型参数化，处理的数据类型不是固定的，而是可以作为参数传入。使用泛型类：
  ```java
    public static void main(String[] args) {
        Pair<Integer> minmax = new Pair<>(1, 100);
        Integer min = minmax.getFirst();
        Integer max = minmax.getSecond();
        System.out.println("min: " + min);
        System.out.println("max: " + max);
    }
  ```
  类型参数可以有多个，Pair类中的first和second可以是不同的类型, 改进后的Pair类定义：
  ```java
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
  ```
* **3.基本原理** <br>
  > 泛型类型参数到底是什么呢？为什么一定要定义类型参数呢？定义普通类，直接使用Object不就行了吗？
  
  比如，Pair类可以写为：
  ```java
    public class Pair {

        Object first;
        Object second;
        
        public Pair(Object first, Object second){
            this.first = first;
            this.second = second;
        }
        
        public Object getFirst() {
            return first;
        }
        
        public Object getSecond() {
            return second;
        }
    }
  ```
  使用Pair类：
  ```java
    Pair minmax = new Pair(1,100);
    Integer min = (Integer)minmax.getFirst();
    Integer max = (Integer)minmax.getSecond();

    Pair kv = new Pair("name","xinyue");
    String key = (String)kv.getFirst();
    String value = (String)kv.getSecond();
  ```
  上述定义正是Java内部泛型的实现原理：
  > 对于泛型类，Java编译器会将泛型代码转换为普通的非泛型代码，就像上面的普通Pair类代码及其使用代码一样，将类型参数T擦除，替换为Object，插入必要的强制类型转换。Java虚拟机实际执行的时候，它是不知道泛型这回事的，它只知道普通的类及代码。

  即**Java泛型是通过擦除实现的**

* **4.泛型的好处**
  > 既然只使用普通类和Object就是可以的，而且泛型最后也转换为了普通类，那为什么还要用泛型呢？或者说，泛型到底有什么好处呢？

  主要有两个好处：<br>
    * **更好的安全性**
    * **更好的可读性**
  
  语言和程序设计的一个重要目标是将bug尽量消灭在摇篮里，即在编译时就发现并报告错误，而不是等到运行时。

  只使用Object，代码写错的时候，开发环境和编译器不能帮我们发现问题：
  ```java
    Pair pair = new Pair("xinyue",1);
    Integer id = (Integer)pair.getFirst();
    String name = (String)pair.getSecond();
  ```
  上述代码编译时并不会报错，但在运行时，程序会抛出类型转换异常ClassCastException。如果使用泛型，则不可能犯这个错误：
  ```java
    Pair<String,Integer> pair = new Pair<>("xinyue",1);
    Integer id = pair.getFirst();
    String name = pair.getSecond();
  ```
  上述代码会在编译时开发环境和编译器就会提示你错误，这称之为**类型安全**

* **5.容器类**
  >泛型类最常见的用途是作为容器类，所谓容器类，简单说就是容纳并管理多项数据的类.

  如Java集合中的`ArrayList`和`HashMap`在使用的时候需要传递相应参数类型：
  ```java
  List<Integer> li = new ArrayList<>();
  Map<String, Integer> map = new HashMap<>();
  ```
  **查看ArrayList内部源码，理解其实现原理。**

* **6.泛型方法** 
  >除了泛型类，方法也可以是泛型的，而且，一个方法是不是泛型的，与它所在的类是不是泛型没有什么关系。
  ```java
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

        public static void main(String[] args) {
            int index = indexOf(new Integer[]{1,3,5}, 3);
            System.out.println(index);
        }
    }
  ```
  这个方法就是一个泛型方法，类型参数为T，放在返回值前面，注意泛型只能使用包装类型(Integer)进行实例化，不能是int。<br>
  多个泛型参数：
  ```java
    public static <U,V> Pair<U,V> makePair(U first, V second){
        Pair<U,V> pair = new Pair<>(first, second);
        return pair;
    }

    //makePair(1,"xinyue");
  ```

* **7.泛型接口** <br>
  接口也可以是泛型的, 如Comparable和Comparator接口：
  ```java
    public interface Comparable<T> {
        public int compareTo(T o);
    }
    public interface Comparator<T> {
        int compare(T o1, T o2);
        boolean equals(Object obj);
    }
  ```
  实现接口时，应该指定具体的类型，比如，对Integer类，实现代码是：
  ```java
    public final class Integer extends Number implements Comparable<Integer>{
        public int compareTo(Integer anotherInteger) {
            return compare(this.value, anotherInteger.value);
        }
        //...
    }
  ```
* **8.类型参数的限定**
  >在之前的介绍中，无论是泛型类、泛型方法还是泛型接口，关于类型参数，我们都知之甚少，只能把它当做Object，但Java支持限定这个参数的一个上界，也就是说，参数必须为给定的上界类型或其子类型，这个限定是通过extends这个关键字来表示的。

  **（1）上界为某个具体类**<br>
  比如说，上面的Pair类，可以定义一个子类NumberPair，限定两个类型参数必须为Number，代码如下：
  ```java
    public class NumberPair<U extends Number, V extends Number> extends Pair<U, V> {
        public NumberPair(U first, V second) {
            super(first, second);
        }
    }
  ```
  限定类型后，就可以使用该类型的方法了，比如说，对于NumberPair类，first和second变量就可以当做Number进行处理了:
  ```java
    public double sum() {
        return getFirst().doubleValue() + getSecond().doubleValue();
    }
  ```
  使用方法：
  ```java
    public static void main(String[] args) {
        NumberPair<Integer,Double> pair = new NumberPair<>(10, 12.34);
        double sum = pair.sum();
        System.out.println(sum);
    }
  ```
  >限定类型后，如果类型使用错误，编译器会提示。指定边界后，类型擦除时就不会转换为Object了，而是会转换为它的边界类型

  **（2）上界为某个接口**<br>
  在泛型方法中，一种常见的场景是限定类型必须实现Comparable接口：
  ```java
    public static <T extends Comparable> T max(T[] arr){
        T max = arr[0];
        for(int i=1; i<arr.length; i++){
            if(arr[i].compareTo(max)>0){
                max = arr[i];
            }
        }
        return max;
    }
  ```
  不过，直接这么写代码，Java中会给一个警告信息，因为Comparable是一个泛型接口，它也需要一个类型参数，所以完整的方法声明应该是：
  ```java
    public static <T extends Comparable<T>> T max(T[] arr){

    //...

    }
  ```
  这种形式称之为**递归类型限制**

  **（3）上界为其他类型参数**<br>
  >上面的限定都是指定了一个明确的类或接口，Java支持一个类型参数以另一个类型参数作为上界.

  具体原因还不是很理解。

>泛型是计算机程序中一种重要的思维方式，它将数据结构和算法与数据类型相分离，使得同一套数据结构和算法，能够应用于各种数据类型，而且还可以保证类型安全，提高可读性.


#### 8.2 泛型通配符
* **为什么需要通配符**
  >正是为了解决保持「向上转型」概念在 Java 语言中的统一，使泛型也支持向上转型，所以 Java 推出了通配符的概念
  
  我们看一个具体的例子：
  ```java
  class Fruit {}
  class Apple extends Fruit {}
  ```
  上述`Apple`类是`Fruit`类的子类，在Java中，我们可以这样写：
  ```java
  Fruit apple = new Apple();
  ```
  使用 Fruit 类型的变量指向了一个 Apple对象，这称之为向上转型。现在我们有一个`Plate`类表示装水果的盘子，其代码如下：
  ```java
  public class Plate<T> {
    private List<T> list;
    public Plate() {list = new ArrayList<>();}
    public void add(T item) {list.add(item);}
    public T get() {return list.get(0);}
  }
  ```
  我们可以这样定义一个装水果的盘子：
  ```java
  Plate<Fruit> plate = new Plate<Fruit>();
  plate.add(new Fruit());
  plate.add(new Apple());
  ```
  按照Java向上转型的原则，我们这样定义：
  ```java
  Plate<Fruit> plate = new Plate<Apple>();  //Error
  ```
  上述代码编译时会报错，原因时泛型并不直接支持向上转型。使用通配符后就可以正常编译了，修改后如下：
  ```java
  Plate<? extends Fruit> plate = new Plate<Apple>();
  ```
  上述我们使用的是上界通配符。
* **无界通配符** <br>
  无边界的通配符的主要作用就是让泛型能够接受未知类型的数据。
  ```java
  Plate<?> plate = new Plate<Fruit>();
  ```
  上述无界通配符也可以改为普通的类型参数。

* **上界通配符** <br>
  使用固定上边界的通配符的泛型, 就能够接受指定类及其子类类型的数据
  ```java
  Plate<? extends Fruit> plate = new Plate<XXX>()
  ```
  上面我们对盘子的定义中，plate 可以指向任何 Fruit 类对象，或者任何 Fruit 的子类对象，我们下面几种定义都是正确的：
  ```java
  Plate<? extends Fruit> plate = new Plate<Apple>();
  Plate<? extends Fruit> plate = new Plate<Fruit>();
  ```
  **注意：使用上界通配符只能读，不能写**
  ```java
  plate<? extends Fruit> plate = new Plate<Apple>();
  //不能存入东西
  p.add(new Fruit()); //Error
  p.add(new Apple()); //Error
  //读取出的东西只能存放在Fruit或其基类里
  Fruit f1 = p.get();
  Object f2 = p.get();
  Apple f3 = p.get(); //Error
  ```
  这是因为我们在使用上界通配符的时候表明容器里是Fruit类或其基类，但具体是什么类型JVM不知道。既然我们不能确定要往里面放的类型，那 JVM 就干脆什么都不给放，避免出错。<br>
  那为什么又可以取出数据呢？因为无论是取出苹果，还是橙子，还是香蕉，我们都可以通过向上转型用 Fruit 类型的变量指向它

* **下界通配符** <br>
  使用固定下边界的通配符的泛型, 就能够接受指定类及其父类类型的数据。
  ```java
  Plate<? super Apple> plate = new Plate<Fruit>();
  ```
  上述表示plate可以指向Apple或Apple的父类。

  **注意：下界通配符可以往里存，但往外取只能放在Object对象里**

* **PECS原则** <br>
  （1）Producer Extends 说的是当你的情景是生产者类型，需要获取资源以供生产时，我们建议使用 extends 通配符，因为使用了 extends 通配符的类型更适合获取资源。<br>
  （2）Consumer Super 说的是当你的场景是消费者类型，需要存入资源以供消费时，我们建议使用 super 通配符，因为使用 super 通配符的类型更适合存入资源。

参考：<br>
1. 《Java编程的逻辑》 第8章 泛型
2. https://www.zhihu.com/question/20400700
3. https://www.imooc.com/article/22909