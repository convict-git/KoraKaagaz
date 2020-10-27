package networking.queueManagement;

/**
 *
 * @author Pandravisam Shiva Santhosh
 */

/*
    This file contains the interface of the Queue and the methods that will be provided
    by the Queue Management module.
    Note that the interface takes a generic type T
 */

public interface IQueue <T> {

    /*
    This method "enqueue" takes a argument item which is of generic type T and it inserts
    into the queue. This method doesn't return anything.
     */
    public void enqueue(T item);

    /*
    This method "dequeue" takes no argument and removes the item which has been
    there for long time in the queue. In other words removes the front item in
    the queue.
     */
    public  void dequeue();

    /*
    This method "front" takes no argument and returns the front item of the queue without
    removing the item.
     */
    public T front();

    /*
    This method "isEmpty" takes no argument and returns a boolean value indicating true if the
    queue is empty and false if the queue is not empty.
     */
    public boolean isEmpty();

    /*
    This method "size" takes no argument and returns a Integer indicating the size of the Queue
     */
    public int size();
}
