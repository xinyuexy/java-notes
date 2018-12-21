package com.xinyue.part2;
/**
 * 内部类使用实例
 */
public class Outer {
    private static int shared = 100;
    private int a = 100;

    //静态内部类
    public static class StaticInner {
        public void innerMethod() {
            System.out.println("inner " + shared);
        }
    }

    //成员内部类
    public class Inner {
        public void innerMethod() {
            //直接访问外部类私有变量
            System.out.println("outer a " +a);
            //通过Outer.this访问
            Outer.this.action();
            //没有歧义的情况下可以不加Outer.this
            action();
        }
    }

    private void action() {
        System.out.println("action");
    }

    public void test() {
        StaticInner si = new StaticInner();
        si.innerMethod();
        Inner inner = new Inner();
        inner.innerMethod();
    }

    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.test();
    }
}
