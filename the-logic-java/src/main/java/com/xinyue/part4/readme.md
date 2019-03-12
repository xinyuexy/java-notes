## Part4 文件

### 第13章 文件基本技术
#### 1. 文件概述
* **文件分类**：文本文件和二进制文件
* **输入输出流**：Java中使用统一的概念：（输入输出流）来处理所有IO（文件，键盘，显示终端，网络等）
* **装饰器模式**：对基本的流的功能进行增强。有两个基类：`FilterInputStream`和`FilterOutputStream`，其他类(如`BufferedInputStream`和`BufferedOutputStream`)继承于此。
* **随机读写文件**：`RandomAccessFile`
* **NIO**：不同的看待IO的方式，缓冲区和通道的概念
* **序列化和反序列化**：序列化是将内存中的对象持久化保存到一个流中，反序列化是从流中恢复对象到内存。XML和JSON

#### 2. 二进制文件和字节流
以二进制方式读写的主要流有:<br>
* InputStream/OutputStream: 这是基类，它们是抽象类。
* FileInputStream/FileOutputStream: 输入源和输出目标是文件的流。
* ByteArrayInputStream/ByteArrayOutputStream: 输入源和输出目标是字节数组的流。
* DataInputStream/DataOutputStream: 装饰类，按基本类型和字符串而非只是字节读写流。
* BufferedInputStream/BufferedOutputStream: 装饰类，对输入输出流提供缓冲功能。

#### 3. 文本文件和字符流
对于文本文件，字节流没有编码的概念，不能按行处理，使用不太方便，更适合的是使用字符流，Java中的主要字符流:
* Reader/Writer：字符流的基类，它们是抽象类。
* InputStreamReader/OutputStreamWriter：适配器类，输入是InputStream，输出是OutputStream，将字节流转换为字符流。（可以指定编码）
* FileReader/FileWriter：输入源和输出目标是文件的字符流。
* CharArrayReader/CharArrayWriter: 输入源和输出目标是char数组的字符流。
* StringReader/StringWriter：输入源和输出目标是String的字符流。
* BufferedReader/BufferedWriter：装饰类，对输入输出流提供缓冲，以及按行读写功能。
* PrintWriter：装饰类，可将基本类型和对象转换为其字符串形式输出的类。
* Scanner类

**标准流**：输入流：`System.in`；输出流`System.out`；错误流`System.err`. **重定向**

### 第14章 文件高级技术
流的方式有几个限制：
* 要么读，要么写，不能同时读和写
* 不能随机读写，只能从头读到尾，且不能重复读，虽然通过缓冲可以实现部分重读，但是有限制。
> Java中还有一个类RandomAccessFile，它没有这两个限制，既可以读，也可以写，还可以随机读写，它是一个更接近于操作系统API的封装类。

#### 1. RandomAccessFile
* **构造方法**
```java
public RandomAccessFile(String name, String mode) throws FileNotFoundException
public RandomAccessFile(File file, String mode) throws FileNotFoundException
```
mode为打开模式：
* "r": 只用于读
* "rw": 用于读和写
* "rws": 和"rw"一样，用于读和写，另外，它要求文件内容和元数据的任何更新都同步到设备上
* "rwd": 和"rw"一样，用于读和写,另外，它要求文件内容的任何更新都同步到设备上，和"rws"的区别是，元数据的更新不要求同步

* **随机访问**
  >RandomAccessFile内部有一个文件指针，指向当前读写的位置，各种read/write操作都会自动更新该指针，与流不同的是，RandomAccessFile可以获取该指针，也可以更改该指针

```java
//获取当前文件指针
public native long getFilePointer() throws IOException;
//更改当前文件指针到pos
public native void seek(long pos) throws IOException;
```

* **键值对数据库简单实现BasicDB**

#### 2. 内存映射文件
>所谓内存映射文件，就是将文件映射到内存，文件对应于内存中的一个字节数组，对文件的操作变为对这个字节数组的操作，而字节数组的操作直接映射到文件上。这种映射可以是映射文件全部区域，也可以是只映射一部分区域。

* **处理处理非常大的文件**
* **不同应用程序之间的通信（消息队列）**
* **设计一个简单的消息队列BasicQueue**

