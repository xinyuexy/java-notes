## Part4 并发

### 第15章 并发基础知识
>线程表示一条单独的执行流，它有自己的程序执行计数器，有自己的栈
#### 1. 创建线程
两种方式：1.继承`Thread`；2.实现`Runnable`接口

* **继承Thread** <br>
```java
public class HelloThread extends Thread {
    @Override
    public void run() {
        System.out.println("thread name: " + Thread.currentThread().getName());
        System.out.println("hello");
    }
    
    public static void main(String[] args) {
        Thread thread = new HelloThread();
        //启动线程
        thread.start();
        //只是调用run方法会在主线程中执行
        thread.run();
    }
}
```
1. 通过重写run方法：run方法类似于单线程程序中的main方法，线程从run方法的第一条语句开始执行直到结束。
2. 调用start方法启动线程：为什么调用的是start，执行的却是run方法呢？start表示启动该线程，使其成为一条单独的执行流，背后，操作系统会分配线程相关的资源，每个线程会有单独的程序执行计数器和栈，操作系统会把这个线程作为一个独立的个体进行调度，分配时间片让它执行，执行的起点就是run方法。直接调用run方法会在主线程中执行。

* **实现Runnable接口**
```java
public class HelloRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("thread name: " + Thread.currentThread().getName());
        System.out.println("hello");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new HelloRunnable());
        //启动线程
        thread.start();
        //只是调用run方法会在主线程中执行
        thread.run();
    }
}
```
>通过继承Thread来实现线程虽然比较简单，但我们知道，Java中只支持单继承，每个类最多只能有一个父类，如果类已经有父类了，就不能再继承Thread，这时，可以通过实现java.lang.Runnable接口来实现线程。

    实现Runnable是不够的，要启动线程，还是要创建一个Thread对象，但传递一个Runnable对象.

#### 2. 线程的基本属性和方法
* **id和name** <br>
  每个线程都有一个id和name，id是一个递增的整数，每创建一个线程就加一，name的默认值是"Thread-"后跟一个编号，name可以在Thread的构造方法中进行指定，也可以通过setName方法进行设置

* **优先级** <br>
  线程有一个优先级的概念，在Java中，优先级从1到10，默认为5，相关方法是：
  ```java
    public final void setPriority(int newPriority)
    public final int getPriority()
  ```

* **状态** <br>
  线程有一个状态的概念，Thread有一个方法用于获取线程的状态：
  ```java
    public State getState()
  ```

* **是否daemo线程** <br>
  >启动线程会启动一条单独的执行流，整个程序只有在所有线程都结束的时候才退出，但daemo线程是例外，当整个程序中剩下的都是daemo线程的时候，程序就会退出。

  >daemo线程有什么用呢？它一般是其他线程的辅助线程，在它辅助的主线程退出的时候，它就没有存在的意义了。在我们运行一个即使最简单的"hello world"类型的程序时，实际上，Java也会创建多个线程，除了main线程外，至少还有一个负责垃圾回收的线程，这个线程就是daemo线程，在main线程结束的时候，垃圾回收线程也会退出

* **sleep方法** <br>
  调用该方法会让当前线程睡眠指定的时间，单位是毫秒

* **yield方法** <br>
  Thread还有一个让出CPU的方法：
  ```java
    public static native void yield();
  ```

* **join方法** <br>
  在前面HelloThread的例子中，HelloThread没执行完，main线程可能就执行完了，Thread有一个join方法，可以让调用join的线程等待该线程结束.

#### 3. 共享内存及问题
* **共享内存** <br>
  每个线程表示一条单独的执行流，有自己的程序计数器，有自己的栈，但线程之间可以共享内存，它们可以访问和操作相同的对象.

* **竞态条件** <br>
  当多个线程访问和操作同一个对象时，最终执行结果与执行时序有关，可能正确也可能不正确.(如多个线程对同一个变量count计数，由于不是**原子操作**)  
  怎么解决这个问题呢？有多种方法:
  1. 使用synchronized关键字
  2. 使用显式锁
  3. 使用原子变量 

* **内存可见性** <br>
  >多个线程可以共享访问和操作相同的变量，但一个线程对一个共享变量的修改，另一个线程不一定马上就能看到，甚至永远也看不到.

  ```java
    public class VisibilityDemo {
        private static boolean shutdown = false;
        
        static class HelloThread extends Thread {
            @Override
            public void run() {
                while(!shutdown){
                    // do nothing
                }
                System.out.println("exit hello");
            }
        }

        public static void main(String[] args) throws InterruptedException {
            new HelloThread().start();
            Thread.sleep(1000);
            shutdown = true;
            System.out.println("exit main");
        }
    }
  ```
  期望的结果是两个线程都退出，但实际执行，很可能会发现HelloThread永远都不会退出，也就是说，在HelloThread执行流看来，shutdown永远为false，即使main线程已经更改为了true。
  >这就是内存可见性问题。在计算机系统中，除了内存，数据还会被缓存在CPU的寄存器以及各级缓存中，当访问一个变量时，可能直接从寄存器或CPU缓存中获取，而不一定到内存中去取，当修改一个变量时，也可能是先写到缓存中，而稍后才会同步更新到内存中。在单线程的程序中，这一般不是个问题，但在多线程的程序中，尤其是在有多CPU的情况下，这就是个严重的问题。一个线程对内存的修改，另一个线程看不到，一是修改没有及时同步到内存，二是另一个线程根本就没从内存读。

  怎么解决这个问题呢？有多种方法：
  1. 使用volatile关键字
  2. 使用synchronized关键字或显式锁同步 

创建线程需要消耗操作系统的资源<br>
线程调度和切换也是有成本的(上下文切换)

#### 4. 理解synchronized
>synchronized 是 Java 中的关键字，是利用锁的机制来实现同步的。

* **锁机制的特性**：<br>
1. **互斥性**：即在同一时间只允许一个线程持有某个对象锁，通过这种特性来实现多线程中的协调机制，互斥性我们也往往称为操作的原子性。
2. **内存可见性**：`synchronized`除了保证原子操作外，它还有一个重要的作用，就是保证内存可见性，在释放锁时，所有写入都会写回内存，而获得锁后，都会从内存中读最新数据。不过，如果只是为了保证内存可见性，使用synchronzied的成本有点高，有一个更轻量级的方式，那就是给变量加修饰符`volatile`
3. **可重入性**：对同一个执行线程，它在获得了锁之后，在调用其他需要同样锁的代码时，可以直接调用
4. **死锁**：怎么解决呢？首先，应该尽量避免在持有一个锁的同时去申请另一个锁，如果确实需要多个锁，所有代码都应该按照相同的顺序去申请锁

synchronized可以用于修饰类的实例方法、静态方法和代码块.<br>

* **实例对象锁与类对象锁** <br>
  锁的对象有以下几种： <br>
  1. 同步非静态方法（synchronized method），锁是当前对象的实例对象。
  2. 同步静态方法（synchronized static method），锁是当前对象的类对象（Class 对象）。
  3. 同步代码块一（synchronized (this)，synchronized (类实例对象)），锁是小括号 () 中的实例对象。
  4. 同步代码块二（synchronized (类.class)），锁是小括号 () 中的类对象（Class 对象）。

    1）实例对象锁，不同的实例拥有不同的实例对象锁，所以对于同一个实例对象，在同一时刻只有一个线程可以访问这个实例对象的同步方法；不同的实例对象，不能保证多线程的同步操作。

    2）类对象锁（全局锁），在 JVM 中一个类只有一个与之对应的类对象，所以在同一时刻只有一个线程可以访问这个类的同步方法。

 * **任意对象都有一个锁和等待队列** <br>
>synchronized同步的对象可以是任意对象，任意对象都有一个锁和等待队列，或者说，任何对象都可以作为锁对象。

```java
public class Counter {
    private int count;
    private Object lock = new Object();
    
    public void incr(){
        synchronized(lock){
            count ++;    
        }
    }
    
    public int getCount() {
        synchronized(lock){
            return count;
        }
    }
} 
```


 * **同步容器及其注意事项** <br>
  
    同步容器

    并发容器

>参考博客(写的很好)：https://segmentfault.com/a/1190000009225706

*要多实践*

#### 5. 线程的基本协作机制
* **协作场景** <br>
  1. 生产者/消费者协作模式
  2. 同时开始
  3. 等待结束: 主从协作模式
  4. 异步结果
  5. 集合点

* **wait/notify** <br>
  >Java在Object类而非Thread类中，定义了一些线程协作的基本方法，使得每个对象都可以调用这些方法，这些方法有两类，一类是wait，另一类是notify。
  
  主要有两个wait方法：
  ```java
    public final void wait() throws InterruptedException
    public final native void wait(long timeout) throws InterruptedException;
  ```
  >每个对象都有一把锁和等待队列，一个线程在进入synchronized代码块时，会尝试获取锁，获取不到的话会把当前线程加入等待队列中，其实，除了用于锁的等待队列，每个对象还有另一个等待队列，表示条件队列，该队列用于线程间的协作。调用wait就会把当前线程放到条件队列上并阻塞，表示当前线程执行不下去了，它需要等待一个条件，这个条件它自己改变不了，需要其他线程改变.

  当其他线程改变了条件后，应该调用Object的notify方法：
  ```java
    public final native void notify();
    public final native void notifyAll();
  ```
  >notify做的事情就是从条件队列中选一个线程，将其从队列中移除并唤醒，notifyAll和notify的区别是，它会移除条件队列中所有的线程并全部唤醒。

  #### 6. 线程的中断
  线程的几种状态