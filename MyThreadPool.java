package practice.my_thread_pool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;



public class MyThreadPool {

	private static int pool_size = 10;
	private WorkThread[] workThread;
	private List<Runnable> taskQueue;
	
	public static void foo() {
		
	}
	
	public MyThreadPool(){
		this(pool_size);
	}
	
	public MyThreadPool(int pool_size){
	  	MyThreadPool.pool_size = pool_size;
	  	taskQueue = new LinkedList<Runnable>();
	  	workThread = new WorkThread[pool_size];
	  	
	  	for(int i = 0;i < pool_size; i++){
	  		workThread[i] = new WorkThread();
	  		workThread[i].start();
	  	}
	}
	
	void shutdown() {
		while(!taskQueue.isEmpty()){
	  		try{
	  			Thread.sleep(5);
	  		}catch(InterruptedException e){
	  			e.printStackTrace();
	  		}
	  	}
	  	for(int i = 0; i < pool_size; i++){
	  		workThread[i].stopRunning();
	  		workThread[i] = null;
	  	}
	  	
	  	taskQueue.clear();
	}
		
	class WorkThread extends Thread {
		private boolean isRunning = true;
		
	  	@Override
	  	public void run(){
	  		Runnable task = null;
	  		while(isRunning){
	  			synchronized(taskQueue){
	  				while(isRunning && taskQueue.isEmpty()){
	  					try{
	  						taskQueue.wait(20);
	  					}catch(InterruptedException e){
	  						e.printStackTrace();
	  					}
	  				}
	  				if(!taskQueue.isEmpty())
	  					task = taskQueue.remove(0);
	  			}
	  			if(task!=null){
	  				task.run();
	  			}
	  			task=null;
	  		}
	  	}
		
	  	public void stopRunning(){
	  		isRunning=false;
	  	}
	}
	
	void submit(Runnable task) {
		synchronized(taskQueue){
	  		taskQueue.add(task);
	  		taskQueue.notify();
	  	}
	}
		

	
	Future submit(Callable task) {
		RunnableFuture future = new FutureTask<>(task);
		synchronized(taskQueue){
			try{
		    	taskQueue.add(future);
		    	taskQueue.notify();
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
		}
	    
	    return future;
	}

	
}
