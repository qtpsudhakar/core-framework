package com.automation.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ExcelParser {

	private final String xlFilePath = System.getProperty("user.dir") + "/src/test/resources/testdata/";

	private static final String xlFileName = "sample-data.xlsx";
	private static final String xlSheetName = "Data";

	static Fillo fillo = new Fillo();
	static Connection connection;
	static Recordset recordset;
	String query = "select * from Data";

	public static void main(String args[]) throws IOException {
		ExcelParser excelDataParser = new ExcelParser();
		excelDataParser.readDataFromExcel(xlFileName, xlSheetName);
		excelDataParser.writeDataIntoExcel(xlFileName, xlSheetName);
		excelDataParser.readExcel(2);
		excelDataParser.getExcelDataAsDatabase();
	}
	
	//get excel data as database

	public void getExcelDataAsDatabase() {
		try {
			connection = fillo.getConnection(xlFilePath + xlFileName);
			recordset = connection.executeQuery(query);
			System.out.println("========================================");
			System.out.println("excel data fetch as database");
			while (recordset.next()) {
				System.out.println(recordset.getField("testCaseName"));
				System.out.println(recordset.getField("username"));
				System.out.println(recordset.getField("password"));
				System.out.println(recordset.getField("errormessage"));

				System.out.println("========================================");
			}

			recordset.close();
			connection.close();
		} catch (FilloException e) {
			e.printStackTrace();
		}
	}

	// Read cell from a row
	public void readExcel(int rowcounter) throws IOException {

		XSSFWorkbook srcBook = new XSSFWorkbook(xlFilePath + xlFileName);
		XSSFSheet sourceSheet = srcBook.getSheetAt(0);
		int rownum = rowcounter;
		XSSFRow sourceRow = sourceSheet.getRow(rownum);
		XSSFCell cell1 = sourceRow.getCell(0);
		XSSFCell cell2 = sourceRow.getCell(1);
		XSSFCell cell3 = sourceRow.getCell(2);
		System.out.println("=========================");
		System.out.println(cell1);
		System.out.println(cell2);
		System.out.println(cell3);
		srcBook.close();
	}

	/**
	 * @author
	 * @param
	 * 
	 * readExcel        file data using apache poi API
	 *
	 */
	public void readDataFromExcel(String xlFileName, String xlSheetName) {

		// Step-1:Create a object of File Class to Open the excel file
		File file = new File(xlFilePath + xlFileName);

		// Step-2:Create object of FileInputStream to read the excel
		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			// Step3:Create Object of XSSFWorkbook class
			Workbook workBook = new XSSFWorkbook(fileInputStream);

			// Step4:Read sheet inside the workbook by its name
			Sheet sheet = workBook.getSheet(xlSheetName);

			// Step5:Find number of rows in the excel file
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			System.out.println("Row Count :" + rowCount);

			// Step6: Get the cell values
			for (int i = 1; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					String val = row.getCell(j).getStringCellValue();
					System.out.println(val);
				}
				System.out.println("=========================");
			}

			// Step7: Close the workbook
			workBook.close();
			fileInputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeDataIntoExcel(String xlFileName, String xlSheetName) {

		// Step-1:Create a object of File Class to Open the excel file
		File file = new File(xlFilePath + xlFileName);

		// Step-2:Create object of FileInputStream to read the excel
		try {

			FileInputStream fileInputStream = new FileInputStream(file);

			// Step3:Create Object of XSSFWorkbook class
			Workbook workBook = new XSSFWorkbook(fileInputStream);

			// Step4:Read sheet inside the workbook by its name
			Sheet sheet = workBook.getSheet(xlSheetName);

			// step5: write the data into a new row
			sheet.getRow(3).createCell(0).setCellValue("TC_4");
			sheet.getRow(3).createCell(1).setCellValue("test@126");
			sheet.getRow(3).createCell(2).setCellValue("password5");
			sheet.getRow(3).createCell(3).setCellValue("Username should not contain any specail characters");

			FileOutputStream fileOutputStream = new FileOutputStream(new File(xlFilePath + xlFileName));
			workBook.write(fileOutputStream);

			// Step6: Close the workbook
			fileOutputStream.flush();
			fileOutputStream.close();
			workBook.close();
			fileInputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
