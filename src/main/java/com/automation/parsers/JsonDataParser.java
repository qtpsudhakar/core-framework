package com.automation.parsers;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;

public class JsonDataParser {

	public static void main(String args[]) {
		Gson gson = new Gson();

		try (Reader reader = new FileReader("c:\\test\\staff.json")) {
			// Convert JSON File to Java Object
			// Staff staff = gson.fromJson(reader, Staff.class);

			// print staff
			// System.out.println(staff);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}