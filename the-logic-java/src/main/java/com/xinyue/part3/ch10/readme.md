## Part3 泛型与容器

### 第10章 Map和Set
#### 10.1 剖析HashMap
>实现map接口，高效查找，键值不能重复，允许null

* **实现原理:** <br>
  HashMap内部有如下几个主要的实例变量：
  ```java
    transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
    transient int size;
    int threshold;
    final float loadFactor;
  ```
  size表示实际键值对的个数，table是一个Entry类型的数组(Java8好像为Node),其中的每个元素指向一个单向链表，链表中的每个节点表示一个键值对.

  **动态扩容** <br>
  `threshold`和`loadFactor`

  **计算哈希值** <br>
  ```java
    final int hash(Object k) {
        int h = 0
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
  ```
  >基于key自身的hashCode方法的返回值，又进行了一些位运算，目的是为了随机和均匀性。

  **查找键值**: <br>
  查找键值比较的时候先比较hash值,hash相同的时候，再使用equals方法进行比较，代码为：
  ```java
  if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
  ```
  >为什么要先比较hash呢？因为hash是整数，比较的性能一般要比equals比较高很多，hash不同，就没有必要调用equals方法了，这样整体上可以提高比较性能。

* **原理总结** <br>
  >以上就是HashMap的基本实现原理，内部有一个数组table，每个元素table[i]指向一个单向链表，根据键存取值，用键算出hash，取模得到数组中的索引位置buketIndex，然后操作table[buketIndex]指向的单向链表。在对应链表操作时也是先比较hash值，相同的话才用equals方法比较，这就要求，相同的对象其hashCode()返回值必须相同，如果键是自定义的类，就特别需要注意这一点。这也是hashCode和equals方法的一个关键约束

* **HashMap特点分析** <br>
  HashMap实现了Map接口，内部使用数组链表和哈希的方式进行实现,这决定了它有如下特点：
  * 根据键保存和获取值的效率都很高，为O(1)，每个单向链表往往只有一个或少数几个节点，根据hash值就可以直接快速定位。
  * HashMap中的键值对没有顺序，因为hash值是随机的。

* **小结** <br>
  >根据哈希值存取对象、比较对象是计算机程序中一种重要的思维方式，它使得存取对象主要依赖于自身哈希值，而不是与其他对象进行比较，存取效率也就与集合大小无关，高达O(1)，即使进行比较，也利用哈希值提高比较性能。

  >不过HashMap没有顺序，如果要保持添加的顺序，可以使用HashMap的一个子类LinkedHashMap。Map还有一个重要的实现类TreeMap，它可以排序.

#### 10.2 剖析HashSet
>Set表示的是没有重复元素、且不保证顺序的容器接口，它扩展了Collection
```java
public interface Set<E> extends Collection<E> {
  ...
}
```
与Collection接口中定义的方法是一样的，不过，一些方法有一些不同的规范要求。<br>
应用场景：排重, 保存特殊值, 集合运算

* **实现原理:** <br>
  内部使用HashMap实现，维护一个HashMap的实例变量：
  ```java
  private transient HashMap<E,Object> map;
  ```
  因为map有键和值，而set相当于只有键，所以HashSet在内部定义了一个虚拟值，所有的键都拥有同一个值：
  ```java
  // Dummy value to associate with an Object in the backing Map
  private static final Object PRESENT = new Object();
  ```
  我们下面看看HashSet的一些构造方法:
  ```java
    /**
    * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
    * default initial capacity (16) and load factor (0.75).
    */
    public HashSet() {
        map = new HashMap<>();
    }

    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }
  ```
  可以看到，`HashSet`的构造方法主要是调用了`HashMap`的构造方法.

  **添加元素**:
  ```java
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
  ```
  add方法调用了`HashMap`的put方法，元素e用于键，值就是那个固定值PRESENT，put返回null表示原来没有对应的键，添加成功了。HashMap中一个键只会保存一份。

  **检查是否包含元素**：
  ```java
  public boolean contains(Object o) {
      return map.containsKey(o);
  }
  ```
  就是检查map中是否包含对应的键。

  **删除元素**:
  ```java
  public boolean remove(Object o) {
    return map.remove(o)==PRESENT;
  }
  ```
  **迭代器**:
  ```java
  public Iterator<E> iterator() {
      return map.keySet().iterator();
  }
  ```
  就是返回map的keySet的迭代器。
  >HashSet没有顺序，如果要保持添加的顺序，可以使用HashSet的一个子类LinkedHashSet。Set还有一个重要的实现类，TreeSet，它可以排序。

#### 10.4 剖析TreeMap <br>
1. 内部基于红黑树实现，按照键值维护元素顺序。
2. 比较器：键值实现Comparable和传入comparator
```java
public TreeMap()
public TreeMap(Comparator<? super K> comparator)
```
第一个为默认构造方法，如果使用默认构造方法，要求Map中的键实现Comparabe接口，TreeMap内部进行各种比较时会调用键的Comparable接口中的compareTo方法。

第二个接受一个比较器对象comparator，如果comparator不为null，在TreeMap内部进行比较时会调用这个comparator的compare方法，而不再调用键的compareTo方法，也不再要求键实现Comparable接口。

3.**实现原理** <br>
4. **特点**：<br>
   * 按键有序，TreeMap同样实现了SortedMap和NavigableMap接口，可以方便的根据键的顺序进行查找，如第一个、最后一个、某一范围的键、邻近键等。 
   * 为了按键有序，TreeMap要求键实现Comparable接口或通过构造方法提供一个Comparator对象。
   * 根据键保存、查找、删除的效率比较高，为O(h)，h为树的高度，在树平衡的情况下，h为log2(N)，N为节点数。

#### 10.5 剖析TreeSet <br>
内部实现基于TreeMap

#### 10.6 剖析LinkedHashMap <br>
继承自HashMap，支持两种顺序：插入顺序和访问顺序。（默认插入顺序）

除了哈希表，内部还维护一个双向链表维护键值对的顺序。每个键值对既位于哈希表中，也位于这个双向链表中。

**实现原理**

**LinkedHashSet**：内部实现基于LinkedHashMap

#### 10.7 剖析EnumMap <br>
键值为枚举类型。对枚举类型效率高。

```java
public enum Size {
  SMALL, MEDIUM, LARGE
}

Map<Size, Integer> map = new EnumMap<>(Size.class);
```

#### 10.8 剖析EnumSet <br>
还不是很懂