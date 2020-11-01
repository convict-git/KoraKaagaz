package networking.java_practice;

import java.util.concurrent.*;
import java.util.*;

public class UserBlockingQueue<T> implements queue<T>{
	int capacity;
	BlockingQueue<T> q;

	UserBlockingQueue (){
		capacity = 100;
		q = new ArrayBlockingQueue<T>(10);
	}

	public void enqueue(T obj){
		try {
			q.put(obj);
		} catch (InterruptedException e){
			System.out.println("Got Interrupted");
		}
		System.out.println(q);
	}
	public static void main(String[] args) {
		queue<String> q1 = new UserBlockingQueue<String>();
		Packet p = new Packet();
		p.setMssg("Shiva");
		q1.enqueue("Santhosh");
		queue<Packet> q2 = new UserBlockingQueue<Packet>();
		q2.enqueue(p);
	}
}