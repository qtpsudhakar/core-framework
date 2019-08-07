package com.automation.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileDataParser {

	private static final String txtFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/";

	public static void readTextFileData() throws IOException {

		// Read Data from text file
		FileReader fileReader = new FileReader(txtFilePath + "sample-data.txt");
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}

		bufferedReader.close();

	}

	public static void writeToTextFile() throws IOException {

		try {
			FileWriter writer = new FileWriter(txtFilePath + "sample-data.txt", true);
			writer.write("\r");
			writer.write("write data");
			writer.write("\t\t");
			writer.write("new test case");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws IOException {
		readTextFileData();
		writeToTextFile();
	}

}
