package networking.queueManagement;

/**
 *
 * This file contains the interface of the Queue and the methods that will be provided by the QueueManagement Module
 *  Note that the interface takes a generic type T.
 *
 * @author Pandravisam Shiva Santhosh
 */

public interface IQueue <T> {

    /** This method "clear" removes all the elements present in the queue */
    public void clear();

    /**
     * This method "dequeue" takes no argument and removes the item which has been there for long time in the queue.
     * In other words removes the front item in the queue.
    */
    public  void dequeue();

    /**
     * This method "enqueue" takes a argument item and it inserts at the tail of the queue.
     * This method doesn't return anything.
     *
     * @param item the data that will go in the queue.
     */
    public void enqueue(T item) ;

    /**
     * This method "front" takes no argument and returns the front item of the queue without
     * removing the item from the queue
     *
     * @return the front item if queue is not empty or else returns NULL
     */
    public T front();

    /**
     * This method "isEmpty" takes no argument and returns a boolean value indicating true if the
     *  queue is empty and false if the queue is not empty.
     *
     * @return true/false depending on the condition.
     */
    public boolean isEmpty();

    /**
     *  This method "size" takes no argument and returns a integer indicating the size of the Queue
     *
     * @return current size of the queue
     */
    public int size();

}
