package com.automation.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Different patterns
 * 
 *[abc]			a, b, or c (simple class)
 *[^abc]		Any character except a, b, or c (negation)
 *[a-zA-Z]		a through z or A through Z, inclusive (range)
 *[a-zA-Z0-9]		a through z or A through Z, inclusive (range)
 *
 */

public class RegularExpression {

	public static void main(String[] args) {

		// 1st way
		Pattern p = Pattern.compile(".s");// . represents single character
		Matcher m = p.matcher("as");
		boolean b = m.matches();
		System.out.println(b);

		// 2nd way
		boolean b2 = Pattern.compile(".s").matcher("as").matches();
		System.out.println(b2);

		// 3rd way
		boolean b3 = Pattern.matches(".s", "as");
		System.out.println(b3);

		//for checking a string alpha numeric
		System.out.println(Pattern.matches("[a-zA-Z0-9]+$", "abcd2"));

	}

}
