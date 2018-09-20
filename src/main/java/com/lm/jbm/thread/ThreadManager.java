package com.lm.jbm.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadManager {
	
	private static class ThreadPool {
		private static final ExecutorService pools = Executors.newCachedThreadPool();
	}
	
	private ThreadManager() {}
	
	public static final ExecutorService getInstance() {
		return ThreadPool.pools;
	} 
	
		
}
