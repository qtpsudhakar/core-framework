package com.automation.utility;

public class SingletonClass {
	public String s;
	private static SingletonClass singletonInstance = null;

	// private constructor
	private SingletonClass() {
		s = "Hello I am a string part of Singleton class";
	}

	// static method to create instance of Singleton class
	public static SingletonClass getInstance() {
		if (singletonInstance == null)
			singletonInstance = new SingletonClass();
		return singletonInstance;
	}

	public static class AccessSingleTon {

		public static void main(String args[]) {

			// instantiating Singleton class with variable x
			SingletonClass x = SingletonClass.getInstance();
			x.s = x.s.toUpperCase();
			System.out.println(x.s);
		}

	}

}
