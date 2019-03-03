## Part3 泛型与容器

### 第12章 通用容器类和总结
#### 12.1 抽象容器类
>所有具体容器类其实都不是从头构建的，它们都继承了一些抽象容器类。这些抽象类提供了容器接口的部分实现，方便了Java具体容器类的实现.

抽象容器类与之前介绍的接口和具体容器类的关系如下图所示：
<img src="https://ws1.sinaimg.cn/mw690/006SQxbply1g0puqhziodj30is0a7mz9.jpg" style="zoom:80%"/>

虚线框表示接口，有Collection, List, Set, Queue, Deque和Map。

有六个抽象容器类：
* `AbstractCollection`: 实现了Collection接口，被抽象类AbstractList, AbstractSet, AbstractQueue继承，ArrayDeque也继承自AbstractCollection (图中未画出)。
* `AbstractList`：父类是AbstractCollection，实现了List接口，被ArrayList, AbstractSequentialList继承。
* `AbstractSequentialList`：父类是AbstractList，被LinkedList继承。
* `AbstractMap`：实现了Map接口，被TreeMap, HashMap, EnumMap继承。
* `AbstractSet`：父类是AbstractCollection，实现了Set接口，被HashSet, TreeSet和EnumSet继承。
* `AbstractQueue`：父类是AbstractCollection，实现了Queue接口，被PriorityQueue继承。

**问题思考**：<br>
1. 每个抽象类实现原理，实现了哪些基本方法.<br>
2. 自己如何通过继承抽象类定义自己的容器。
3. `AbstractList`和`AbstractSequentialList`实现区别

>实现了容器接口，就可以方便的参与到容器类这个大家庭中进行相互协作，也可以方便的利用Collections这个类实现的通用算法和功能.

#### 12.2 Collections
Java中有一个类Collections，提供了很多针对容器接口的通用功能，这些功能都是以静态方法的方式提供的，都有哪些功能呢？大概可以分为两类：<br>
* 1.对容器接口对象进行操作
* 2.返回一个容器接口对象 

对于第一类，操作大概可以分为三组：
* 查找和替换
* 排序和调整顺序
* 添加和修改 
  
对于第二类，大概可以分为两组：
* 适配器：将其他类型的数据转换为容器接口对象
* 装饰器：修饰一个给定容器接口对象，增加某种性质 

**基本方法：**<br>
**1. 二分查找：** <br>
Collections提供了针对List接口的二分查找，如下所示：
```java
public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key)
public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c) 
```
一个要求List的每个元素实现Comparable接口，另一个不需要，但要求提供Comparator。
具体查找代码如下：
```java
    public static <T>
    int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        if (list instanceof RandomAccess || list.size()<BINARYSEARCH_THRESHOLD)
            return Collections.indexedBinarySearch(list, key);
        else
            return Collections.iteratorBinarySearch(list, key);
    }
```
可以看到会根据传入的List是否实现了随机访问接口或者元素个数比较少来调用indexedBinarySearch根据索引直接访问中间元素进行查找，否则调用iteratorBinarySearch使用迭代器的方式访问中间元素进行查找。

二分查找假定List中的元素是从小到大排序的。如果是从大到小排序的，则需要传入一个逆序Comparator对象：
```java
List<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{
        35, 24, 13, 12, 8, 7, 1
}));
System.out.println(Collections.binarySearch(list, 7, Collections.reverseOrder()));
//out: 5
```

**2. 查找最大值/最小值** <br>
```java
public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp)
public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll)
public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comp)
```

**3. 查找元素出现次数** <br>
```java
public static int frequency(Collection<?> c, Object o)
```

**4. 查找子List** <br>
在source List中查找target List的位置:
```java
public static int indexOfSubList(List<?> source, List<?> target)
public static int lastIndexOfSubList(List<?> source, List<?> target)
```
indexOfSubList从开头找，lastIndexOfSubList从结尾找，没找到返回-1，找到返回第一个匹配元素的索引位置.

**5. 查看两个集合是否有交集** <br>
```java
public static boolean disjoint(Collection<?> c1, Collection<?> c2)
```

**6. 排序** <br>
```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
public static <T> void sort(List<T> list, Comparator<? super T> c)
```

**7. 翻转列表顺序** <br>
```java
public static void reverse(List<?> list) 
```

**8. 随机化重排** <br>
```java
public static void shuffle(List<?> list)
public static void shuffle(List<?> list, Random rnd)
```
从后往前遍历列表，逐个给每个位置重新赋值，值从前面的未重新赋值的元素中随机挑选.

**适配器** <br>
>适配器就是将一种类型的接口转换成另一种接口

Collections类提供了几组类似于适配器的方法：
* 空容器方法：类似于将null或"空"转换为一个标准的容器接口对象
* 单一对象方法：将一个单独的对象转换为一个标准的容器接口对象
* 其他适配方法：将Map转换为Set等
  
**装饰器** <br>
>装饰器接受一个接口对象，并返回一个同样接口的对象，不过，新对象可能会扩展一些新的方法或属性，扩展的方法或属性就是所谓的"装饰"，也可能会对原有的接口方法做一些修改，达到一定的"装饰"目的。

Collections有三组装饰器方法，它们的返回对象都没有新的方法或属性，但改变了原有接口方法的性质，经过"装饰"后，它们更为安全了，具体分别是写安全、类型安全和线程安全:
* 写安全
```java
public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c)
public static <T> List<T> unmodifiableList(List<? extends T> list)
public static <K,V> Map<K,V> unmodifiableMap(Map<? extends K, ? extends V> m)
public static <T> Set<T> unmodifiableSet(Set<? extends T> s)
public static <K,V> SortedMap<K,V> unmodifiableSortedMap(SortedMap<K, ? extends V> m)
public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> s)
```
顾名思义，这组unmodifiableXXX方法就是使容器对象变为只读的，写入会抛出UnsupportedOperationException异常。为什么要变为只读的呢？典型场景是，需要传递一个容器对象给一个方法，这个方法可能是第三方提供的，为避免第三方误写，所以在传递前，变为只读的

* 类型安全
```java
public static <E> Collection<E> checkedCollection(Collection<E> c, Class<E> type)
public static <E> List<E> checkedList(List<E> list, Class<E> type)
public static <K, V> Map<K, V> checkedMap(Map<K, V> m, Class<K> keyType, Class<V> valueType)
public static <E> Set<E> checkedSet(Set<E> s, Class<E> type)
public static <K,V> SortedMap<K,V> checkedSortedMap(SortedMap<K, V> m, Class<K> keyType, Class<V> valueType)
public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> s, Class<E> type)
```
使用这组checkedXXX方法，都需要传递类型对象，这些方法都会使容器对象的方法在运行时检查类型的正确性，如果不匹配，会抛出ClassCastException异常.

* 线程安全 <br>
之前我们介绍的各种容器类都不是线程安全的，也就是说，如果多个线程同时读写同一个容器对象，是不安全的。Collections提供了一组方法，可以将一个容器对象变为线程安全的，如下所示：
```java
public static <T> Collection<T> synchronizedCollection(Collection<T> c)
public static <T> List<T> synchronizedList(List<T> list)
public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m)
public static <T> Set<T> synchronizedSet(Set<T> s)
public static <K,V> SortedMap<K,V> synchronizedSortedMap(SortedMap<K,V> m)
public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s)
```
>需要说明的，这些方法都是通过给所有容器方法加锁来实现的，这种实现并不是最优的，Java提供了很多专门针对并发访问的容器类

#### 12.3 容器类总结
除了`HashTable`、`Vector`和`Stack`是线程安全的，其他的容器类都不是线程安全的。也就是说，如果多个线程同时读写同一个容器对象，是不安全的。如果需要线程安全，可以使用Collections提供的synchronizedXXX方法对容器对象进行同步，或者使用线程安全的专门容器类。

最后总结的很好，反复看，反复领会：<br>
https://www.cnblogs.com/swiftma/p/6124015.html