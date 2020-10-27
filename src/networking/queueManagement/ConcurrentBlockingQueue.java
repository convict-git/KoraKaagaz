package networking.queueManagement;

import java.util.concurrent.*;

/**
 *
 * @author Pandravisam Shiva Santhosh
 */

public class ConcurrentBlockingQueue<T> implements IQueue<T> {
    int capacity;
    BlockingQueue<T> q;

    ConcurrentBlockingQueue(){
        capacity = 100000;
        q = new ArrayBlockingQueue<T>(capacity);
    }

    public void enqueue(T item){
        try {
            q.put(item);
        } catch (InterruptedException e){
            //throws System.out.println("Got Interrupted");
        }
    }

    public void dequeue(){
        try {
            T item = q.take();
        } catch (InterruptedException e){
            //System.out.println("Got Interrupted");
        }
    }

    public T front(){
        T frontItem = q.peek();
        return frontItem;
    }

    public boolean isEmpty(){
        if(q.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int size(){
        int queueSize = q.size();
        return queueSize;
    }

}
