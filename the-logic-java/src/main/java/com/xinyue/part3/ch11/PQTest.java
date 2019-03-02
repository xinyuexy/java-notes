package com.xinyue.part3.ch11;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class PQTest {
    public static void main(String[] args) {
        Queue<Integer> pq = new PriorityQueue<>(11, Collections.reverseOrder());
        pq.offer(10);
        pq.offer(20);
        pq.addAll(Arrays.asList(new Integer[] {11,12,34,2}));

        while(pq.peek() != null) {
            System.out.println(pq.poll() + " ");
        }
    }
}