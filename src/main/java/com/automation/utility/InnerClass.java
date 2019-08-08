package com.automation.utility;

public class InnerClass {
	
	private int counter = 30;

	public static void main(String[] args) {

		// access non-static inner class members
		InnerClass innerClass = new InnerClass();
		ChildClass childClass = innerClass.new ChildClass();
		System.out.println(childClass.data);

		// access static inner class members
		InnerClass.ChildStaticClass obj = new InnerClass.ChildStaticClass();
		System.out.println(obj.data1);
		
		//local inner class
		InnerClass innerClass1 = new InnerClass();
		innerClass1.testMethod();

	}

	// non-static inner class
	class ChildClass {
		String data = "non-static inner class";

	}

	// static inner class
	static class ChildStaticClass {
		String data1 = "static inner class";

	}


	public void testMethod() {

		// local inner class
		class LocalClass {
			String data2 = "local inner class";
			void msg() {
				System.out.println(data2);
				System.out.println(counter);
			}
		}
		
		LocalClass localClass = new LocalClass();
		localClass.msg();
	}

}
