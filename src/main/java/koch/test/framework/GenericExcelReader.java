package koch.test.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenericExcelReader {
	public Map<String, List<String>> getDataFromExcel(String excelPath, String sheetName) {
		Sheet excelSheet = getDataSheetFromExcel(excelPath, sheetName);
		int columnCount = getNumOfColumnsFromExcelSheet(excelSheet);
		Map<String, List<String>> excelDataMap = createMapKeyFromFirstRow(excelSheet, columnCount);
		return getAllExcelRowData(excelSheet, excelDataMap);

	}

	private Sheet getDataSheetFromExcel(String excelPath, String sheetName) {
		File excelFile = new File(excelPath);
		Workbook excelWorkbook = null;
		Sheet excelSheet = null;
		try {
			FileInputStream fisExcel = new FileInputStream(excelFile);
			excelWorkbook = new XSSFWorkbook(fisExcel);
			excelSheet = excelWorkbook.getSheet(sheetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return excelSheet;
	}
	
	private int getNumOfRowsFromExcelSheet(Sheet excelSheet) {
		return excelSheet.getLastRowNum() - excelSheet.getFirstRowNum();
	}

	private int getNumOfColumnsFromExcelSheet(Sheet excelSheet) {
		return excelSheet.getRow(0).getLastCellNum();
	}
	
	public Map<String, List<String>> createMapKeyFromFirstRow(Sheet excelSheet, int columnCount) {
		Map<String, List<String>> dataMap = new LinkedHashMap<String, List<String>>();
		for (int j = 0; j < columnCount; ++j) {
			dataMap.put(excelSheet.getRow(0).getCell(j).getStringCellValue(), new ArrayList<String>());
		}
		return dataMap;

	}
	
	private Map<String, List<String>> getAllExcelRowData(Sheet excelSheet, Map<String, List<String>> excelDataMap) {
		int rowCount = getNumOfRowsFromExcelSheet(excelSheet);
		int columnCount = getNumOfColumnsFromExcelSheet(excelSheet);
		for(int j = 0; j < columnCount ; ++j) {
			List<String> dataRowList = new ArrayList<String>();
			String columnHeader = excelSheet.getRow(0).getCell(j).getStringCellValue();
			for(int i = 1 ; i <= rowCount ; ++i) {
				String rowValue = excelSheet.getRow(i).getCell(j).getStringCellValue();
				dataRowList.add(rowValue);
			}
			excelDataMap.put(columnHeader, dataRowList);
		}
		return excelDataMap;
	}

}
