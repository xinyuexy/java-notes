## Part3 泛型与容器

### 第9章 列表和队列
#### 9.1 剖析ArrayList
* **基本原理** <br>
  ArrayList内部是基于一个动态扩容的数组实现的，其实例变量声明如下：
  ```java
  private static final int DEFAULT_CAPACITY = 10;
  private static final Object[] EMPTY_ELEMENTDATA = {};
  private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
  transient Object[] elementData;
  private int size;
  ```
  上述`elementData`用于存储实际数据，`size`变量记录实际元素的个数，`elementData`会随着实际元素个数的增多而重新分配。<br>
  我们可以看看ArrayList的几个构造函数源码:
  ```java
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
  ```
  上述构造函数传入一个初始容量，如果这个初始容量大于0，则创建相应大小的数组并赋值给`elementData`，如果为0，则将其初始化为空数组`EMPTY_ELEMENTDATA`(用于共享的空数组实例)
  
  ```java
    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
  ```
  第二个构造函数没有传入容量参数，则初始化为`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`默认空数组实例，这个和之前的`EMPTY_ELEMENTDATA`有什么不同呢？虽然都是空数组实例，但是`EMPTY_ELEMENTDATA`是用户传入的容量为0时初始化为空，而`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`是用于默认大小的空实例（为了后续当add第一个元素时，应该分配多少容量。

  **add方法源码如下：**
  ```java
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
  ```
  首先调用`ensureCapacityInternal`确保容量是够的，其代码如下：
  ```java
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
  ```
  里面首先调用`calculateCapacity`函数计算要增加的容量，代码如下：
  ```java
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }
  ```
  首先判断`elementData`是不是默认空实例数组(即第一次添加的时候)，是的话分配的大小为`DEFAULT_CAPACITY`和`minCapacity(size+1)`二者较大值。首次分配的时候`minCapacity`为1，所以分配的大小为`DEFAULT_CAPACITY`为10.<br>
  然后调用`ensureExplicitCapacity`函数，代码如下：
  ```java
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
  ```
  里面有个`modCount`变量，而且每次加1，表示内部的修改次数。`grow`方法为实际分配容量的代码如下：
  ```java
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
  ```
  **remove方法源码如下：**
  ```java
    public E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }
  ```
  上述计算要移动的元素个数，从index往后的元素都要往前移动一位，实际调用`System.arraycopy`方法移动元素，然后`elementData[--size] = null;`将size减1，并将最后一个元素设为null表示不再引用原来对象，从而可以被垃圾回收器回收。

  **get方法源码如下：**
  ```java
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }
  ```
  其调用`elementData`方法，其代码如下：
  ```java
    E elementData(int index) {
        return (E) elementData[index];
    }
  ```
  由于`elementData`里的元素为`Object`类型，需要进行强制转换。

* **迭代：** <br>
  **(1) foreach循环：**
  ```java
    ArrayList<Integer> intList = new ArrayList<Integer>();
    intList.add(123);
    intList.add(456);
    intList.add(789);
    for(Integer a : intList){
        System.out.println(a);
    }
  ```
  编译器会将它转换为类似如下代码：
  ```java
    Iterator<Integer> it = intList.iterator();
    while(it.hasNext()){
        System.out.println(it.next());
    }
  ```
  `ArrayList`实现了`Iterable`接口（实际上是通过实现Collection来继承的），`Iterable`接口表示可迭代，主要定义如下：
  ```java
    public interface Iterable<T> {
        Iterator<T> iterator();
    }
  ```
  就是要求实现iterator方法，返回一个实现了Iterator接口的对象（实际的迭代器），Iterator接口主要定义如下：
  ```java
    public interface Iterator<E> {
        boolean hasNext();
        E next();
        void remove();
    }
  ```
  **`ListIterator`**: `ArrayList`除了提供基本的`iterator`方法外，还提供了两个返回`ListIterator`接口的方法：
  ```java
    public ListIterator<E> listIterator()
    public ListIterator<E> listIterator(int index)
  ```
  ListIterator扩展了Iterator接口，增加了一些方法，向前遍历、添加元素、修改元素、返回索引位置等，添加的方法有：
  ```java
    public interface ListIterator<E> extends Iterator<E> {
        boolean hasPrevious();
        E previous();
        int nextIndex();
        int previousIndex();
        void set(E e);
        void add(E e);
    }
  ```
  **(2) 迭代器实现原理**：<br>
  `iterator()`迭代器：
  ```java
    public Iterator<E> iterator() {
        return new Itr();
    }
  ```
  上述方法返回了一个Itr对象，Itr对象的声明如下:
  ```java
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;
        ...
    }
  ```
  Itr类继承了`Iterator`接口，实现了其中的`hashNext`，`next`, `remove`等方法，具体如何实现的需要仔细阅读源码。

  `listIterator()`迭代器：
  ```java
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }
  ```
  其返回的是`ListItr`对象，其声明如下：
  ```java
    private class ListItr extends Itr implements ListIterator<E> {
        ...
    }
  ```
  除了`Iterator`的方法，还实现了`previous`, `hasPrevious`，即可以前向遍历。<br>
  通过上述我们可以看到，我们可以在类中通过继承`Iterator`等接口实现自定义迭代器类。

  **(3) 迭代器的好处**：<br>
  >迭代器语法更为通用，它适用于各种容器类。<br>
  >迭代器表示的是一种关注点分离的思想，将数据的实际组织方式与数据的迭代遍历相分离，是一种常见的设计模式<br>
  >从封装的思路上讲，迭代器封装了各种数据组织方式的迭代操作，提供了简单和一致的接口。

* **ArrayList实现的接口**<br>
  ArrayList还实现了三个主要的接口Collection, List和RandomAccess.

  **`Collection`**: Collection表示一个数据集合，数据间没有位置或顺序的概念.<br>
  接口定义为：
  ```java
    public interface Collection<E> extends Iterable<E> {
        int size();
        boolean isEmpty();
        boolean contains(Object o);
        Iterator<E> iterator();
        Object[] toArray();
        <T> T[] toArray(T[] a);
        boolean add(E e);
        boolean remove(Object o);
        boolean containsAll(Collection<?> c);
        boolean addAll(Collection<? extends E> c);
        boolean removeAll(Collection<?> c);
        boolean retainAll(Collection<?> c);
        void clear();
        boolean equals(Object o);
        int hashCode();
    }
  ```
  **`List`**: List表示有顺序或位置的数据集合，它扩展了Collection，增加的主要方法有：
  ```java
    boolean addAll(int index, Collection<? extends E> c);
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);
    int indexOf(Object o);
    int lastIndexOf(Object o);
    ListIterator<E> listIterator();
    ListIterator<E> listIterator(int index);
    List<E> subList(int fromIndex, int toIndex);
  ```
  **`RandomAccess:`** 其定义为：
  ```java
    public interface RandomAccess {
    }
  ```
  >没有定义任何代码。这有什么用呢？这种没有任何代码的接口在Java中被称之为**标记接口**，用于声明类的一种属性。

    实现了RandomAccess接口的类表示可以随机访问，可随机访问就是具备类似数组那样的特性

#### 9.2 剖析LinkedList

#### 9.3 剖析ArrayDeque