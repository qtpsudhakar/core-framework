package com.automation.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {

	public static void main(String[] args) {

		String content = "books.com";

		String pattern = ".*book.*";
		boolean isMatch = Pattern.matches(pattern, content);
		System.out.println("The text contains 'book'? " + isMatch);
		
		
		String content1 = "This is a tutorial Website!";
		String patternString = ".*tuToRiAl.";
		Pattern pattern1 = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		
		String content2 = "This is a tutorial Website!";
		String patternString1 = ".*tuToRiAl.*";
		Pattern pattern2 = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern2.matcher(content);
		boolean isMatched = matcher.matches();
		System.out.println("Is it a Match?" + isMatched);

	}

}
