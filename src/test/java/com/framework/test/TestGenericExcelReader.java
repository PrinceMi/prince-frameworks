package com.framework.test;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import koch.test.framework.GenericExcelReader;

public class TestGenericExcelReader {
	
	String dataFilePath = System.getProperty("user.dir") + "\\datafile.xlsx";
	String sheetName = "data";
	
	@Test
	public void verifyGenericExcelReader() {
		
		Map<String, List<String>> data = new GenericExcelReader().getDataFromExcel(dataFilePath, sheetName);
		int maxSize = 0;
		for(String key: data.keySet())
			maxSize = Math.max(maxSize, data.get(key).size());
		
		for(int i = 0 ; i < maxSize ; ++i) {
			for(String key: data.keySet()) {
				System.out.print(data.get(key).get(i) + " ");
			}
			System.out.println();
		}
	}

}
