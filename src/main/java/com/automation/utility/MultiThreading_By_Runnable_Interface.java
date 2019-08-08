package com.automation.utility;

/*
 * MultiThreading is a Java feature that allows concurrent execution of two or more parts of a program 
 * for maximum utilization of CPU. 
 * Each part of such program is called a thread. 
 * So, threads are light-weight processes within a process.
 * Threads can be created by using two mechanisms :
 * 1. Extending the Thread class
 * 2. Implementing the Runnable Interface
 */

public class MultiThreading_By_Runnable_Interface implements Runnable {

	private Thread t;
	private String threadName;

	// 2 Create Threads by using Runnable Interface

	MultiThreading_By_Runnable_Interface(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run() {
		System.out.println("Running " + threadName);
		try {
			for (int i = 2; i > 0; i--) {
				System.out.println("Thread: " + threadName + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void start() {
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	// Main Class
	static class Multithread {
		public static void main(String[] args) throws InterruptedException {
			MultiThreading_By_Runnable_Interface R1 = new MultiThreading_By_Runnable_Interface("Thread-1");
			R1.start();

			MultiThreading_By_Runnable_Interface R2 = new MultiThreading_By_Runnable_Interface("Thread-2");
			R2.start();
		}

	}

}