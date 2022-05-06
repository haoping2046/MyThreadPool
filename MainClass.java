package practice.my_thread_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


public class MainClass {
	
	public static class DummyTask implements Runnable {
		private int id;
		
		public DummyTask(int id) {
			super();
			this.id = id;
		}

		@Override
		public void run() {
			System.out.println("My thread pool: " + Thread.currentThread().getName() + "----->" + id);
		}
	}
	
	public static void testMyThreadPool() {
		MyThreadPool es = new MyThreadPool();
		
		for (int i=0; i<100; i++) {
			es.submit(new DummyTask(i));
		}
		es.shutdown();
	}
	
	public static void testFuture() {
		MyThreadPool es = new MyThreadPool();
		
		for (int i=0; i<100; i++) {
			es.submit(new DummyTask(i));
		}
		es.shutdown();
	}
	
	public static void main(String[] args) {
		testMyThreadPool();
//		testFuture();
	}
	
}
