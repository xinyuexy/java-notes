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