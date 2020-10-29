package networking.queueManagement;

import java.util.concurrent.*;

/**
 *  This file contains the class ConcurrentBlockingQueue which implements the the queue interface
 *  IQueue.This class can be used to create a Queue Data-Structure which is used to store and also retrieve
 *  the data in FIFO (First In First Out) manner.
 *
 *  To create a Queue, one can declare
 *  IQueue<T> q = new ConcurrentBlockingQueue<T>();
 *  where T is of any builtin or user-defined data-type/structure/class.
 *
 * @author Pandravisam Shiva Santhosh
 */

public class ConcurrentBlockingQueue<T> implements IQueue<T> {

    /**
     * It contains the maximum number of elements that queue can store.
     */
    int capacity;

    /**
     * Declaring the the java inbuilt Blocking Queue which is thread-safe and will  not rise the concurrency issues
     */
    BlockingQueue<T> q;

    /**
     * Constructor which initialises capacity and creates a ArrayBlocking queue instance of size equal to
     *  capacity.
     */
    public ConcurrentBlockingQueue(){
        capacity = 100000;
        q = new ArrayBlockingQueue<T>(capacity);
    }

    /**
     *  This method "clear" removes all the elements present in the queue
     */
    public void clear(){
        q.clear();
    }

    /**
     * This method "dequeue" takes no argument and removes the item which has been there for long time in the queue.
     * In other words removes the front item in the queue.
     */
    public void dequeue(){
        try {
            T item = q.take();
        } catch (InterruptedException e){
            System.out.println("The ex");
            /**/
        }
    }

    /**
     * This method "enqueue" takes a argument item and it inserts at the tail of the queue.
     * This method doesn't return anything.
     *
     * @param item the data that will go in the queue.
     */
    public void enqueue(T item){
        try {
            q.put(item);
        } catch (InterruptedException e){
            //throws System.out.println("Exception has been raised");
        }
    }

    /**
     *This method "front" takes no argument and returns the front item of the queue without
     * removing the item from the queue
     *
     * @return the front item if queue is not empty or else returns NULL
     */
    public T front(){
        return q.peek();
    }

    /**
     * This method "isEmpty" takes no argument and returns a boolean value indicating true if the
     *  queue is empty and false if the queue is not empty.
     *
     * @return true/false depending on the condition.
     */
    public boolean isEmpty(){
        return (q.size() == 0);
    }

    /**
     *  This method "size" takes no argument and returns a integer indicating the size of the Queue
     *
     * @return current size of the queue
     */
    public int size(){
        return q.size();
    }
}
