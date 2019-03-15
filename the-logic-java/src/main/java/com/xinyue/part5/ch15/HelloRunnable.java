package com.xinyue.part5.ch15;

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