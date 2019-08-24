package com.automation.utilities;

/*
 * MultiThreading is a Java feature that allows concurrent execution of two or more parts of a program 
 * for maximum utilization of CPU. 
 * Each part of such program is called a thread. 
 * So, threads are light-weight processes within a process.
 * Threads can be created by using two mechanisms :
 * 1. Extending the Thread class
 * 2. Implementing the Runnable Interface
 */

public class MultiThreadingByThreadClass extends Thread {

	// 1 = Thread creation by extending java.lang.Thread class

	// step1: override the run method of Thread class
	public void run() {
		try {
			// Displaying the thread that is running
			System.out.println("Thread " + Thread.currentThread().getId() + " is running");

		} catch (Exception e) {
			System.out.println("Exception in Thread : " + e.getMessage());
		}
	}

	public static class CreateMultipleThread {
		public static void main(String[] args) {
			int n = 8;
			for (int i = 0; i < 8; i++) {
				MultiThreadingByThreadClass multiThreading = new MultiThreadingByThreadClass();

				// Step2: to create a new Thread
				multiThreading.start();
			}
		}
	}

}