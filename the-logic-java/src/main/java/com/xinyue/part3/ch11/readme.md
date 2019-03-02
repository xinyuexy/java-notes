## Part3 泛型与容器

### 第11章 堆与优先队列
#### 11.1 堆的概念与算法

#### 11.2 剖析PriorityQueue
PriorityQueue实现了Queue接口, Queue定义如下
```java
public interface Queue<E> extends Collection<E> {
    boolean add(E e);   //在尾部添加元素,队列满时抛异常
    boolean offer(E e); //在尾部添加元素,队列满时返回false
    E remove(); //删除头部元素,队列空时抛异常
    E poll();   //删除头部元素,队列空时返回null
    E element();    //查看头部元素,队列空时抛异常
    E peek();   //查看头部元素,队列空时返回null
}
```

`PriorityQueue`主要构造方法：
```java
public PriorityQueue()
public PriorityQueue(int initialCapacity)
public PriorityQueue(int initialCapacity, Comparator<? super E> comparator)
public PriorityQueue(Collection<? extends E> c)
```
为了保持一定顺序，PriorityQueue要求，要么元素实现Comparable接口，要么传递一个比较器Comparator.

基本例子：
```java
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(10);
pq.add(22);
pq.addAll(Arrays.asList(new Integer[]{
    11, 12, 34, 2, 7, 4, 15, 12, 8, 6, 19, 13 }));
while(pq.peek()!=null){
    System.out.print(pq.poll() + " ");
}

//2 4 6 7 8 10 11 12 12 13 15 19 22 34 
```
如果希望时从大到小输出呢，则需要传入一个逆序的Comparator：
```java
Queue<Integer> pq = new PriorityQueue<>(11, Collections.reverseOrder());
```
第一个参数`initialCapacity`设为默认值11

* **实现原理** <br>
  看源码

#### 11.3 堆和PriorityQueue的应用
* **求前K个最大的元素** <br>
  元素数量较多，动态添加的

  **方法**：维护一个容量为k的最小堆，每次来一个新元素都将其与堆顶元素(最小值)进行比较，若小于最小值则丢弃，否则将其加入堆中(调整堆结构)

  ```java
    public class TopK <E> {
        private PriorityQueue<E> p;
        private int k;
        
        public TopK(int k){
            this.k = k;
            this.p = new PriorityQueue<>(k);
        }

        public void addAll(Collection<? extends E> c){
            for(E e : c){
                add(e);
            }
        }
        
        public void add(E e) {
            if(p.size()<k){
                p.add(e);
                return;
            }
            Comparable<? super E> head = (Comparable<? super E>)p.peek();
            if(head.compareTo(e)>0){
                //小于TopK中的最小值，不用变
                return;
            }
            //新元素替换掉原来的最小值成为Top K之一。
            p.poll();
            p.add(e);
        }
        
        public <T> T[] toArray(T[] a){
            return p.toArray(a);
        }

        public E getKth(){
            return p.peek();
        }
    }    
  ```

* **求中值** <br>
  可以使用两个堆，一个最大堆，一个最小堆，思路如下: <br>
  1. 假设当前的中位数为m，最大堆维护的是<=m的元素，最小堆维护的是>=m的元素，但两个堆都不包含m。
  2. 当新的元素到达时，比如为e，将e与m进行比较，若e<=m，则将其加入到最大堆中，否则将其加入到最小堆中。
  3. 第二步后，如果此时最小堆和最大堆的元素个数的差值>=2 ，则将m加入到元素个数少的堆中，然后从元素个数多的堆将根节点移除并赋值给m。

```java
public class Median <E> {
    private PriorityQueue<E> minP; // 最小堆
    private PriorityQueue<E> maxP; //最大堆
    private E m; //当前中值
    
    public Median(){
        this.minP = new PriorityQueue<>();
        this.maxP = new PriorityQueue<>(11, Collections.reverseOrder());
    }
    
    private int compare(E e, E m){
        Comparable<? super E> cmpr = (Comparable<? super E>)e;
        return cmpr.compareTo(m);
    }
    
    public void add(E e){
        if(m==null){ //第一个元素
            m = e;
            return;
        }
        if(compare(e, m)<=0){
            //小于中值, 加入最大堆
            maxP.add(e);
        }else{
            minP.add(e);
        }
        if(minP.size()-maxP.size()>=2){
            //最小堆元素个数多，即大于中值的数多
            //将m加入到最大堆中，然后将最小堆中的根移除赋给m
            maxP.add(this.m);
            this.m = minP.poll();
        }else if(maxP.size()-minP.size()>=2){
            minP.add(this.m);
            this.m = maxP.poll();
        }
    }
    
    public void addAll(Collection<? extends E> c){
        for(E e : c){
            add(e);
        }
    }
    
    public E getM() {
        return m;
    }
}
```