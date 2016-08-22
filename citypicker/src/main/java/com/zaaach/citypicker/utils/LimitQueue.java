package com.zaaach.citypicker.utils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LinkedList已经实现了Queue接口
 * Created by mini on 16/8/21.
 * @param <E>
 */
public class LimitQueue<E> {

    /**
     * 队列长度，实例化类的时候指定
     */
    private int limit;

    LinkedList<E> queue = new LinkedList<E>();

    public LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 获取限制大小
     */
    public int getLimit(){
        return limit;
    }

    /**
     * 获取队列
     */
    public Queue<E> getQueue(){
        return queue;
    }

    public int getQueueSize() {
        return queue.size();
    }

    /**
     * 入队
     */
    public boolean offer(E e) {
        if(queue.size() >= limit){
            //如果超出长度,入队时,先出队
            queue.poll();
        }
        return queue.offer(e);
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public E getFirst() {
        return queue.getFirst();
    }
}
