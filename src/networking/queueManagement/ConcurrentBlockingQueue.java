package networking.queueManagement;

import java.util.concurrent.*;

import infrastructure.validation.logger.*;


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

    /** It contains the maximum number of elements that queue can store */
    int capacity;

    /** Declaring the the java inbuilt Blocking Queue which is thread-safe and will  not rise the concurrency issues */
    BlockingQueue<T> q;

    /** Logger object from the LoggerFactory to log Messages */
    ILogger logger = LoggerFactory.getLoggerInstance();

    /**
     * Default Constructor which initialises capacity and creates a ArrayBlocking queue instance of size equal to
     * capacity.
     */
    public ConcurrentBlockingQueue(){
        capacity = 100000;
        q = new ArrayBlockingQueue<T>(capacity);
        logger.log(
                ModuleID.NETWORKING,
                LogLevel.SUCCESS,
                "A new queue has been constructed which is thread-safe and blocking"
        );
    }

    /**
     * Overloaded Constructor which initialises capacity and creates a ArrayBlocking queue instance of size equal to
     *  capacity.
     *
     * @param capacity the maximum number of elements that queue can hold.
     */
    public ConcurrentBlockingQueue(int capacity){
        this.capacity = capacity;
        q = new ArrayBlockingQueue<T>(this.capacity);
        logger.log(
                ModuleID.NETWORKING,
                LogLevel.SUCCESS,
                "A new queue has been constructed which is thread-safe and blocking"
        );
    }

    /** This method "clear" removes all the elements present in the queue */
    public void clear(){
        q.clear();
    }

    /**
     * This method "dequeue" takes no argument and removes the item which has been there for long time in the queue.
     * In other words removes the front item in the queue.
     */
    public void dequeue(){
        try {
           q.take();
        } catch (InterruptedException e){
            logger.log(
                    ModuleID.NETWORKING,
                    LogLevel.ERROR,
                    "Raised in QueueManagement because a interruption is caused while the queue is waiting"
            );
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
            logger.log(
                    ModuleID.NETWORKING,
                    LogLevel.ERROR,
                    "Raised in QueueManagement because a interruption is caused while the queue is waiting"
            );
        } catch(NullPointerException e){
            logger.log(
                    ModuleID.NETWORKING,
                    LogLevel.ERROR,
                    "Raised in QueueManagement because the element inserted is NULL which should not be inserted"
            );
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
    public int size() {
        return q.size();
    }
}
